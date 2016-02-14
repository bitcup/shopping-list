package com.bitcup.service;

import com.bitcup.entity.ShoppingItem;
import com.bitcup.entity.ShoppingList;

import java.util.List;

/**
 * @author bitcup
 */
public interface ShoppingListService {
    List<ShoppingList> getAllLists(String owner);

    ShoppingList getList(String owner, String id);

    ShoppingList getListByName(String owner, String listName);

    ShoppingList addList(String owner, ShoppingList list);

    void addItemToList(String owner, String listId, ShoppingItem item);

    void deleteList(String owner, String listId);

    void updateList(String owner, ShoppingList list);

    void clearItemsInList(String owner, String listId);

    void removeItemById(String owner, String itemId);

    void removeItemByNameFromList(String owner, String listId, String itemName);

    void updateItem(String owner, ShoppingItem item);
}
