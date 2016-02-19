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
            if (toUpdate.getItems() == null) {
                toUpdate.setItems(new ArrayList<>());
            }
            toUpdate.getItems().clear();
            for (ShoppingItem item : list.getItems()) {
                if (item.getId() == null) {
                    item.setId(UUID.randomUUID().toString());
                }
                toUpdate.getItems().add(item);
            }
            LOGGER.info("updated list: {} for user {}", list, owner);
        }
    }
}
