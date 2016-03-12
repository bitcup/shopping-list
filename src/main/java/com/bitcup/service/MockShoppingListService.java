package com.bitcup.service;

import com.bitcup.dto.ShoppingItemDto;
import com.bitcup.dto.ShoppingListDto;
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

    private Map<String, List<ShoppingListDto>> data = Maps.newConcurrentMap();

    @Override
    public List<ShoppingListDto> getAllLists(String owner) {
        List<ShoppingListDto> lists = data.get(owner);
        LOGGER.info("found lists: {} for user {}", lists, owner);
        return lists;
    }

    @Override
    public ShoppingListDto getList(String owner, String listId) {
        ShoppingListDto list = data.get(owner).stream().filter(shoppingList -> shoppingList.getId().equals(listId)).findFirst().get();
        LOGGER.info("found list: {} for user {}", list, owner);
        return list;
    }

    @Override
    public List<ShoppingListDto> addList(String owner, String listName) {
        ShoppingListDto list = new ShoppingListDto();
        list.setId(UUID.randomUUID().toString());
        list.setName(listName);
        if (!data.containsKey(owner)) {
            data.put(owner, Lists.newArrayList());
        }
        //data.get(owner).add(0, list);
        data.get(owner).add(list);
        LOGGER.info("list: {} created for user {}", list, owner);
        return data.get(owner);
    }

    @Override
    public List<ShoppingListDto> deleteList(String owner, String listId) {
        ShoppingListDto list = getList(owner, listId);
        LOGGER.info("deleting list: {} for user {}", list, owner);
        data.get(owner).remove(list);
        return data.get(owner);
    }

    @Override
    public List<ShoppingListDto> renameList(String owner, String listId, String listName) {
        ShoppingListDto list = getList(owner, listId);
        list.setName(listName);
        return data.get(owner);
    }

    @Override
    public List<ShoppingListDto> clearListItems(String owner, String listId) {
        ShoppingListDto list = getList(owner, listId);
        list.setItems(Lists.newArrayList());
        return data.get(owner);
    }

    @Override
    public ShoppingListDto addItemToList(String owner, String listId, String itemName) {
        ShoppingListDto list = getList(owner, listId);
        ShoppingItemDto item = new ShoppingItemDto();
        item.setId(UUID.randomUUID().toString());
        item.setName(itemName);
        list.getItems().add(item);
        return list;
    }

    @Override
    public ShoppingListDto deleteItemFromList(String owner, String listId, String itemId) {
        ShoppingListDto list = getList(owner, listId);
        ShoppingItemDto toRemove = null;
        for (ShoppingItemDto item : list.getItems()) {
            if (item.getId().equals(itemId)) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            list.getItems().remove(toRemove);
        }
        return list;
    }

    @Override
    public ShoppingListDto renameItemInList(String owner, String listId, String itemId, String itemName) {
        ShoppingListDto list = getList(owner, listId);
        for (ShoppingItemDto item : list.getItems()) {
            if (item.getId().equals(itemId)) {
                item.setName(itemName);
                break;
            }
        }
        return list;
    }

    @Override
    public ShoppingListDto togglePurchasedForItemInList(String owner, String listId, String itemId) {
        ShoppingListDto list = getList(owner, listId);
        for (ShoppingItemDto item : list.getItems()) {
            if (item.getId().equals(itemId)) {
                item.setPurchased(!item.isPurchased());
                break;
            }
        }
        return list;
    }
}
