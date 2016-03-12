package com.bitcup.service;

import com.bitcup.dto.ShoppingItemDto;
import com.bitcup.dto.ShoppingListDto;
import com.bitcup.dynamo.ShoppingListHelper;
import com.bitcup.entity.ShoppingLists;
import com.bitcup.repository.ShoppingListsRepository;
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
    private ShoppingListsRepository repository;

    @Override
    public List<ShoppingListDto> getAllLists(String owner) {
        ShoppingLists lists = repository.findByUserId(owner);
        return ShoppingListHelper.fromDynamoModel(lists);
    }

    @Override
    public ShoppingListDto getList(String owner, String listId) {
        ShoppingLists lists = repository.findByUserId(owner);
        return ShoppingListHelper.fromDynamoModel(lists, listId);
    }

    @Override
    public List<ShoppingListDto> addList(String owner, String listName) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        ShoppingListDto newList = new ShoppingListDto();
        newList.setId(UUID.randomUUID().toString());
        newList.setName(listName);
        allLists.add(newList);
        createOrUpdate(owner, allLists);
        return allLists;
    }

    @Override
    public List<ShoppingListDto> deleteList(String owner, String listId) {
        ShoppingLists lists = repository.findByUserId(owner);
        ShoppingListDto listDto = ShoppingListHelper.fromDynamoModel(lists, listId);
        if (listDto != null) {
            List<ShoppingListDto> listDtos = ShoppingListHelper.fromDynamoModel(lists);
            listDtos.remove(listDto);
            if (listDtos.size() > 0) {
                lists.setListsJson(ShoppingListHelper.toDynamoModel(listDtos));
                repository.save(lists);
            } else {
                repository.delete(lists);
            }
        }
        return getAllLists(owner);
    }

    @Override
    public List<ShoppingListDto> renameList(String owner, String listId, String listName) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                dto.setName(listName);
                break;
            }
        }
        createOrUpdate(owner, allLists);
        return allLists;
    }

    @Override
    public List<ShoppingListDto> clearListItems(String owner, String listId) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                dto.getItems().clear();
                break;
            }
        }
        createOrUpdate(owner, allLists);
        return allLists;
    }

    @Override
    public ShoppingListDto addItemToList(String owner, String listId, String itemName) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        ShoppingListDto toUpdate = null;
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                toUpdate = dto;
                ShoppingItemDto itemDto = new ShoppingItemDto();
                itemDto.setId(UUID.randomUUID().toString());
                itemDto.setName(itemName);
                dto.getItems().add(itemDto);
                break;
            }
        }
        createOrUpdate(owner, allLists);
        return toUpdate;
    }

    @Override
    public ShoppingListDto deleteItemFromList(String owner, String listId, String itemId) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        ShoppingListDto toUpdate = null;
        ShoppingItemDto toDelete = null;
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                toUpdate = dto;
                for (ShoppingItemDto itemDto : dto.getItems()) {
                    if (itemDto.getId().equals(itemId)) {
                        toDelete = itemDto;
                        break;
                    }
                }
                break;
            }
        }
        if (toDelete != null) {
            toUpdate.getItems().remove(toDelete);
            createOrUpdate(owner, allLists);
        }
        return toUpdate;
    }

    @Override
    public ShoppingListDto renameItemInList(String owner, String listId, String itemId, String itemName) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        ShoppingListDto toUpdate = null;
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                toUpdate = dto;
                for (ShoppingItemDto itemDto : dto.getItems()) {
                    if (itemDto.getId().equals(itemId)) {
                        itemDto.setName(itemName);
                        break;
                    }
                }
                break;
            }
        }
        if (toUpdate != null) {
            createOrUpdate(owner, allLists);
        }
        return toUpdate;
    }

    @Override
    public ShoppingListDto togglePurchasedForItemInList(String owner, String listId, String itemId) {
        List<ShoppingListDto> allLists = getAllLists(owner);
        ShoppingListDto toUpdate = null;
        for (ShoppingListDto dto : allLists) {
            if (dto.getId().equals(listId)) {
                toUpdate = dto;
                for (ShoppingItemDto itemDto : dto.getItems()) {
                    if (itemDto.getId().equals(itemId)) {
                        itemDto.setPurchased(!itemDto.isPurchased());
                        break;
                    }
                }
                break;
            }
        }
        if (toUpdate != null) {
            createOrUpdate(owner, allLists);
        }
        return toUpdate;
    }

    private ShoppingLists createOrUpdate(String owner, List<ShoppingListDto> allLists) {
        ShoppingLists lists = repository.findByUserId(owner);
        if (lists == null) {
            lists = new ShoppingLists();
            lists.setUserId(owner);
        }
        lists.setListsJson(ShoppingListHelper.toDynamoModel(allLists));
        repository.save(lists);
        return lists;
    }
}
