package com.bitcup.service;

import com.bitcup.entity.ShoppingItem;
import com.bitcup.entity.ShoppingList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void addItemToList(String owner, String listId, ShoppingItem item) {
        ShoppingList list = getList(owner, listId);
        item.setId(UUID.randomUUID().toString());
        LOGGER.info("adding item: {} to list: {} for user {}", item, list, owner);
        if (list.getItems() == null) {
            list.setItems(new ArrayList<>());
        }
        list.getItems().add(0, item);
    }

    @Override
    public void deleteList(String owner, String listId) {
        ShoppingList list = getList(owner, listId);
        LOGGER.info("deleting list: {} for user {}", list, owner);
        data.get(owner).remove(list);
    }

    @Override
    public void updateList(String owner, ShoppingList list) {
        ShoppingList toUpdate = getList(owner, list.getId());
        if (toUpdate != null) {
            toUpdate.setName(list.getName());
            LOGGER.info("updated list: {} for user {}", list, owner);
        }
    }

    @Override
    public void clearItemsInList(String owner, String listId) {
        ShoppingList list = getList(owner, listId);
        list.getItems().clear();
        LOGGER.info("cleared list: {} for user {}", list, owner);
    }

    @Override
    public void removeItemById(String owner, String itemId) {
        ShoppingItem toRemove = findItem(owner, itemId);
        if (toRemove != null) {
            for (ShoppingList sl : data.get(owner)) {
                sl.getItems().remove(toRemove);
                LOGGER.info("removed item: {} for user {}", toRemove, owner);
            }
        }
    }

    @Override
    public void removeItemByNameFromList(String owner, String listId, String itemName) {

    }

    @Override
    public void updateItem(String owner, ShoppingItem item) {
        ShoppingItem toUpdate = findItem(owner, item.getId());
        if (toUpdate != null) {
            toUpdate.setPurchased(item.isPurchased());
            toUpdate.setName(item.getName());
            LOGGER.info("updated item: {} for user {}", toUpdate, owner);
        }
    }

    private ShoppingItem findItem(String owner, String itemId) {
        for (ShoppingList sl : data.get(owner)) {
            for (ShoppingItem item : sl.getItems()) {
                if (item.getId().equals(itemId)) {
                    return item;
                }
            }
        }
        return null;
    }
}
