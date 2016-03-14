package com.bitcup.controller;

import com.bitcup.dto.ShoppingListDto;
import com.bitcup.dto.UserDto;
import com.bitcup.dto.UserKeyDto;
import com.bitcup.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Qualifier("dynamoShoppingListService")
    //@Qualifier("mockShoppingListService")
    @Autowired
    private ShoppingListService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public UserKeyDto login(@RequestBody UserDto user, HttpServletResponse response) {
        if (user.getUsername().equals("test") && user.getPassword().equals("test")) {
            return new UserKeyDto("abc", "1234567890");
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new UserKeyDto();
    }

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public ResponseEntity<?> getAllLists(HttpServletRequest request) {
        return new ResponseEntity<>(service.getAllLists(getUserId(request)), HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}", method = RequestMethod.GET)
    public ResponseEntity<?> getList(HttpServletRequest request, @PathVariable("listId") String listId) {
        return new ResponseEntity<>(service.getList(getUserId(request), listId), HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/byName/{listName}", method = RequestMethod.GET)
    public ResponseEntity<?> getListByName(HttpServletRequest request, @PathVariable("listName") String listName) {
        return new ResponseEntity<>(service.getListByName(getUserId(request), listName), HttpStatus.OK);
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public ResponseEntity<?> addList(HttpServletRequest request, @RequestBody String listName) {
        List<ShoppingListDto> lists = service.addList(getUserId(request), listName);
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(dto.getId()).toUri());
        return new ResponseEntity<>(lists, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/lists/{listId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteList(HttpServletRequest request, @PathVariable("listId") String listId) {
        List<ShoppingListDto> lists = service.deleteList(getUserId(request), listId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(lists, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/clearItems", method = RequestMethod.POST)
    public ResponseEntity<?> clearListItems(HttpServletRequest request, @PathVariable("listId") String listId) {
        List<ShoppingListDto> lists = service.clearListItems(getUserId(request), listId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(lists, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/renameList", method = RequestMethod.POST)
    public ResponseEntity<?> renameList(HttpServletRequest request, @PathVariable("listId") String listId, @RequestBody String listName) {
        List<ShoppingListDto> lists = service.renameList(getUserId(request), listId, listName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(lists, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/addItem", method = RequestMethod.POST)
    public ResponseEntity<?> addItemToList(HttpServletRequest request, @PathVariable("listId") String listId, @RequestBody String itemName) {
        ShoppingListDto list = service.addItemToList(getUserId(request), listId, itemName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/deleteItem/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItemFromList(HttpServletRequest request, @PathVariable("listId") String listId, @PathVariable("itemId") String itemId) {
        ShoppingListDto list = service.deleteItemFromList(getUserId(request), listId, itemId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/deleteItemByName/{itemName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItemByNameFromList(HttpServletRequest request, @PathVariable("listId") String listId, @PathVariable("itemName") String itemName) {
        ShoppingListDto list = service.deleteItemByNameFromList(getUserId(request), listId, itemName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/renameItem/{itemId}", method = RequestMethod.POST)
    public ResponseEntity<?> renameItem(HttpServletRequest request, @PathVariable("listId") String listId, @PathVariable("itemId") String itemId, @RequestBody String itemName) {
        ShoppingListDto list = service.renameItemInList(getUserId(request), listId, itemId, itemName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/lists/{listId}/togglePurchased", method = RequestMethod.POST)
    public ResponseEntity<?> togglePurchased(HttpServletRequest request, @PathVariable("listId") String listId, @RequestBody String itemId) {
        ShoppingListDto list = service.togglePurchasedForItemInList(getUserId(request), listId, itemId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(listId).toUri());
        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);
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
