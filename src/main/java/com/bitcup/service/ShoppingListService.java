package com.bitcup.service;

import com.bitcup.dto.ShoppingListDto;

import java.util.List;

/**
 * @author bitcup
 */
public interface ShoppingListService {

    List<ShoppingListDto> getAllLists(String owner);

    ShoppingListDto getList(String owner, String listId);

    List<ShoppingListDto> addList(String owner, String listName);

    List<ShoppingListDto> deleteList(String owner, String listId);

    List<ShoppingListDto> renameList(String owner, String listId, String listName);

    List<ShoppingListDto> clearListItems(String owner, String listId);

    ShoppingListDto addItemToList(String owner, String listId, String itemName);

    ShoppingListDto deleteItemFromList(String owner, String listId, String itemId);

    ShoppingListDto renameItemInList(String owner, String listId, String itemId, String itemName);

    ShoppingListDto togglePurchasedForItemInList(String owner, String listId, String itemId);

}
