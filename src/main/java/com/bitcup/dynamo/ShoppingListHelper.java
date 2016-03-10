package com.bitcup.dynamo;

import com.bitcup.dto.ShoppingItemDto;
import com.bitcup.dto.ShoppingListDto;
import com.bitcup.entity.ShoppingList;
import com.bitcup.entity.ShoppingListId;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

/**
 * @author bitcup
 */
public class ShoppingListHelper {
    // assumes: dynamoLists are from a single user
    public static List<ShoppingListDto> fromDynamoModel(Collection<ShoppingList> dynamoLists) {
        TreeMap<ShoppingListKey, ShoppingListDto> dtoMap = new TreeMap<>();
        for (ShoppingList dynamoList : dynamoLists) {
            ShoppingListKey key = new ShoppingListKey(dynamoList.getListId(), dynamoList.getListOrder());
            if (!dtoMap.containsKey(key)) {
                ShoppingListDto listDto = new ShoppingListDto();
                listDto.setId(dynamoList.getListId());
                listDto.setName(dynamoList.getListName());
                dtoMap.put(key, listDto);
            }
            if (dynamoList.getItemId() != null) {
                ShoppingItemDto itemDto = new ShoppingItemDto();
                itemDto.setId(dynamoList.getItemId());
                itemDto.setName(dynamoList.getItemName());
                itemDto.setPurchased(dynamoList.isPurchased());
                dtoMap.get(key).getItems().add(dynamoList.getItemOrder(), itemDto);
            }
        }
        return new ArrayList<>(dtoMap.values());
    }

//    public static Collection<ShoppingList> toDynamoModel(String userId, ShoppingListDto shoppingListDto) {
//        if (shoppingListDto.getItems().isEmpty()) {
//            ShoppingList list = new ShoppingList();
//            list.setUserId(userId);
//            list.setListId(UUID.randomUUID().toString());
//            list.setListName(shoppingListDto.getName());
//            list.setListOrder(0);
//            return Lists.newArrayList(list);
//        } else {
//            List<ShoppingList> lists = Lists.newArrayList();
//            int i = 0;
//            for (ShoppingItemDto itemDto : shoppingListDto.getItems()) {
//                ShoppingList list = new ShoppingList();
//                list.setUserId(userId);
//                list.setListId(UUID.randomUUID().toString());
//                list.setListName(shoppingListDto.getName());
//                list.setListOrder(0);
//            }
//        }
//
//        int i = 0;
//            boolean createNew = shoppingListDto.getId() == null;
//    }

    private static class ShoppingListKey implements Comparable<ShoppingListKey> {
        private final String listId;
        private final int listOrder;

        public ShoppingListKey(String listId, int listOrder) {
            this.listId = listId;
            this.listOrder = listOrder;
        }

        @Override
        public int compareTo(ShoppingListKey o) {
            if (this.listOrder == o.listOrder) {
                return 0;
            }
            return this.listOrder > o.listOrder ? 1 : -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            ShoppingListKey that = (ShoppingListKey) o;

            return new EqualsBuilder()
                    .append(listOrder, that.listOrder)
                    .append(listId, that.listId)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(listId)
                    .append(listOrder)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("listId", listId)
                    .append("listOrder", listOrder)
                    .toString();
        }
    }
}
