package com.bitcup.service;

import com.bitcup.entity.ShoppingList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author bitcup
 */
@Service
public class MockShoppingListService implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockShoppingListService.class);

    private Map<String, List<ShoppingList>> data = Maps.newConcurrentMap();

    @Override
    public List<ShoppingList> getAllLists(String owner) {
        List<ShoppingList> lists = data.get(owner);
        LOGGER.info("found lists: {} for user {}", lists, owner);
        return lists;
    }

    @Override
    public ShoppingList getList(String owner, String id) {
        ShoppingList list = data.get(owner).stream().filter(shoppingList -> shoppingList.getId().equals(id)).findFirst().get();
        LOGGER.info("found list: {} for user {}", list, owner);
        return list;
    }

    @Override
    public ShoppingList getListByName(String owner, String listName) {
        return data.get(owner).stream().filter(shoppingList -> shoppingList.getName().equals(listName)).findFirst().get();
    }

    @Override
    public ShoppingList addList(String owner, ShoppingList list) {
        list.setId(UUID.randomUUID().toString());
        if (!data.containsKey(owner)) {
            data.put(owner, Lists.newArrayList());
        }
        data.get(owner).add(0, list);
        LOGGER.info("list: {} created for user {}", list, owner);
        return list;
    }

    @Override
    public void addItemToList(String owner, String listId, String itemName) {
        ShoppingList list = getList(owner, listId);
        Map<String, Object> item = Maps.newHashMap();
        item.put("id", UUID.randomUUID().toString());
        item.put("purchased", false);
        item.put("name", itemName);
        list.getItems().add(0, item);
    }

    @Override
    public void deleteList(String owner, String listId) {
        ShoppingList list = getList(owner, listId);
        data.get(owner).remove(list);
    }

    @Override
    public void clearItemsInList(String owner, String listId) {
        ShoppingList list = getList(owner, listId);
        list.getItems().clear();
    }

    @Override
    public void removeItemById(String owner, String itemId) {
        Map<String, Object> toRemove = findItem(owner, itemId);
        if (toRemove != null) {
            for (ShoppingList sl : data.get(owner)) {
                sl.getItems().remove(toRemove);
            }
        }
    }

    @Override
    public void removeItemByNameFromList(String owner, String listId, String itemName) {

    }

    @Override
    public void togglePurchased(String owner, String itemId) {
        Map<String, Object> toToggle = findItem(owner, itemId);
        if (toToggle != null) {
            toToggle.put("purchased", !(Boolean) toToggle.get("purchased"));
        }
    }

    private Map<String, Object> findItem(String owner, String itemId) {
        for (ShoppingList sl : data.get(owner)) {
            for (Map<String, Object> item : sl.getItems()) {
                if (item.get("id").equals(itemId)) {
                    return item;
                }
            }
        }
        return null;
    }
}
