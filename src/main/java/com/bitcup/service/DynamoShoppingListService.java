package com.bitcup.service;

import com.bitcup.dto.ShoppingListDto;
import com.bitcup.dynamo.ShoppingListHelper;
import com.bitcup.entity.ShoppingList;
import com.bitcup.entity.ShoppingListId;
import com.bitcup.repository.ShoppingListRepository;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author bitcup
 */
@Service
public class DynamoShoppingListService implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockShoppingListService.class);

    @Autowired
    private ShoppingListRepository repository;

    @Override
    public List<ShoppingListDto> getAllLists(String owner) {
        List<ShoppingList> lists = repository.findAllByUserId(owner);
        return ShoppingListHelper.fromDynamoModel(lists);
    }

    @Override
    public ShoppingListDto getList(String owner, String listId) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        if (lists.isEmpty()) {
            return null;
        }
        List<ShoppingListDto> dto = ShoppingListHelper.fromDynamoModel(lists);
        Preconditions.checkArgument(dto.size() == 1, "Found more than one list");
        return dto.get(0);
    }

    @Override
    public List<ShoppingListDto> addList(String owner, String listName) {
        List<ShoppingListDto> existingLists = getAllLists(owner);
        ShoppingListId shoppingListId = new ShoppingListId();
        shoppingListId.setListId(UUID.randomUUID().toString());
        shoppingListId.setUserId(owner);
        ShoppingList list = new ShoppingList(shoppingListId);
        list.setListName(listName);
        list.setListOrder(existingLists.size());
        repository.save(list);
        return getAllLists(owner);
    }

    @Override
    public List<ShoppingListDto> deleteList(String owner, String listId) {
        ShoppingListId shoppingListId = new ShoppingListId();
        shoppingListId.setListId(listId);
        shoppingListId.setUserId(owner);
        repository.delete(shoppingListId);
        return getAllLists(owner);
    }

    @Override
    public List<ShoppingListDto> renameList(String owner, String listId, String listName) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        for (ShoppingList list : lists) {
            list.setListName(listName);
        }
        repository.save(lists);
        return getAllLists(owner);
    }

    @Override
    public List<ShoppingListDto> clearListItems(String owner, String listId) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        List<ShoppingList> toDelete = Lists.newArrayList();
        ShoppingList toClear = null;
        for (ShoppingList list : lists) {
            if (list.getItemOrder() > 0) {
                toDelete.add(list);
            } else {
                toClear = list;
            }
        }
        if (!toDelete.isEmpty()) {
            repository.delete(toDelete);
        }
        if (toClear != null) {
            toClear.setItemId(null);
            toClear.setItemName(null);
            toClear.setItemOrder(0);
            toClear.setPurchased(false);
            repository.save(toClear);
        }
        return getAllLists(owner);
    }

    @Override
    public ShoppingListDto addItemToList(String owner, String listId, String itemName) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        ShoppingList list = lists.get(0);
        if (lists.size() == 1) {
            // case: no items already exist
            if (list.getItemId() == null) {
                list.setItemId(UUID.randomUUID().toString());
                list.setItemName(itemName);
                list.setItemOrder(0);
                list.setPurchased(false);
                repository.save(list);
            }
            // case: one item already exists
            else {
                // create shopping list row
                ShoppingListId shoppingListId = new ShoppingListId();
                shoppingListId.setListId(listId);
                shoppingListId.setUserId(owner);
                ShoppingList listToAdd = new ShoppingList(shoppingListId);
                listToAdd.setListName(list.getListName());
                listToAdd.setListOrder(list.getListOrder());
                // add item info to it
                listToAdd.setItemId(UUID.randomUUID().toString());
                listToAdd.setItemName(itemName);
                listToAdd.setItemOrder(lists.size());
                listToAdd.setPurchased(false);
                repository.save(listToAdd);
            }
        } else {
            // create shopping list row
            ShoppingListId shoppingListId = new ShoppingListId();
            shoppingListId.setListId(listId);
            shoppingListId.setUserId(owner);
            ShoppingList listToAdd = new ShoppingList(shoppingListId);
            listToAdd.setListName(list.getListName());
            listToAdd.setListOrder(list.getListOrder());
            // add item info to it
            listToAdd.setItemId(UUID.randomUUID().toString());
            listToAdd.setItemName(itemName);
            listToAdd.setItemOrder(lists.size());
            listToAdd.setPurchased(false);
            repository.save(listToAdd);
        }
        return getList(owner, listId);
    }

    @Override
    public ShoppingListDto deleteItemFromList(String owner, String listId, String itemId) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        if (lists.size() == 1) {
            ShoppingList list = lists.get(0);
            list.setItemId(null);
            list.setItemName(null);
            list.setItemOrder(0);
            list.setPurchased(false);
            repository.save(list);
        } else {
            List<ShoppingList> toDecreaseOrder = Lists.newArrayList();
            ShoppingList toDelete = null;
            for (ShoppingList list : lists) {
                if (list.getItemId().equals(itemId)) {
                    toDelete = list;
                    break;
                }
            }
            if (toDelete != null) {
                for (ShoppingList list : lists) {
                    if (list.getItemOrder() > toDelete.getItemOrder()) {
                        list.setItemOrder(list.getItemOrder() - 1);
                        toDecreaseOrder.add(list);
                    }
                }
                repository.delete(toDelete);
                repository.save(toDecreaseOrder);
            }
        }
        return getList(owner, listId);
    }

    @Override
    public ShoppingListDto renameItemInList(String owner, String listId, String itemId, String itemName) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        for (ShoppingList list : lists) {
            if (list.getItemId().equals(itemId)) {
                list.setItemName(itemName);
                repository.save(list);
                break;
            }
        }
        return getList(owner, listId);
    }

    @Override
    public ShoppingListDto togglePurchasedForItemInList(String owner, String listId, String itemId) {
        List<ShoppingList> lists = repository.findAllByUserIdAndListId(owner, listId);
        for (ShoppingList list : lists) {
            if (list.getItemId().equals(itemId)) {
                list.setPurchased(!list.isPurchased());
                repository.save(list);
                break;
            }
        }
        return getList(owner, listId);
    }
}
