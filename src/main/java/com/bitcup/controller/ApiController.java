package com.bitcup.controller;

import com.bitcup.entity.ShoppingList;
import com.bitcup.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * todo: follow http://www.restapitutorial.com/lessons/httpmethods.html
 *
 * @author bitcup
 */
@RestController
@RequestMapping("/api/v1.0")
public class ApiController {

    private static final String REQUEST_AUTH_HEADER = "Authorization";
    private static final String REQUEST_AUTH_SEPARATOR = ":";

    @Autowired
    private ShoppingListService service;

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public ResponseEntity<?> getAllLists(HttpServletRequest request) {
        return new ResponseEntity<>(service.getAllLists(getUserId(request)), HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}", method = RequestMethod.GET)
    public ResponseEntity<?> getList(HttpServletRequest request, @PathVariable("listId") String listId) {
        return new ResponseEntity<>(service.getList(getUserId(request), listId), HttpStatus.OK);
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public ResponseEntity<?> createList(HttpServletRequest request, @RequestBody ShoppingList list) {
        service.addList(getUserId(request), list);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(list.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/lists/{listId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteList(HttpServletRequest request, @PathVariable("listId") String listId) {
        service.deleteList(getUserId(request), listId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateList(HttpServletRequest request, @PathVariable("listId") String listId, @RequestBody ShoppingList list) {
        service.updateList(getUserId(request), list);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }

    private String getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader(REQUEST_AUTH_HEADER);
        if (authHeader == null) {
            throw new ApiAuthException("Missing authentication header");
        }
        String[] userAndSig = authHeader.split(REQUEST_AUTH_SEPARATOR);
        if (userAndSig.length != 2) {
            throw new ApiAuthException("Malformed authentication header");
        }
        String userId = userAndSig[0];
        if (userId == null) {
            throw new ApiAuthException("Missing user");
        }
        // todo: validate signature against request, or throw ApiSignatureException
        String signature = userAndSig[1];
        if (signature == null) {
            throw new ApiSignatureException("Missing signature");
        }
        return userId;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error")
    public String handleAppException(Exception ex) {
        return ex.getMessage();
    }
}
