package com.bitcup.service;

import com.bitcup.entity.ShoppingList;

import java.util.List;

/**
 * @author bitcup
 */
public interface ShoppingListService {
    List<ShoppingList> getAllLists(String owner);

    ShoppingList getList(String owner, String id);

    ShoppingList addList(String owner, ShoppingList list);

    void deleteList(String owner, String listId);

    void updateList(String owner, ShoppingList list);
}
