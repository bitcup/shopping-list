package com.bitcup.dynamo;

import com.bitcup.dto.ShoppingListDto;
import com.bitcup.entity.ShoppingLists;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * @author bitcup
 */
public class ShoppingListHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<ShoppingListDto>> TYPE_REF =
            new TypeReference<List<ShoppingListDto>>() {
            };

    public static List<ShoppingListDto> fromDynamoModel(ShoppingLists dynamoLists) {
        if (dynamoLists == null) {
            return Lists.newArrayList();
        }
        return fromJSON(TYPE_REF, dynamoLists.getListsJson());
    }

    public static ShoppingListDto singleFromDynamoModelById(ShoppingLists dynamoLists, String listId) {
        List<ShoppingListDto> all = fromDynamoModel(dynamoLists);
        return all.stream().filter(dto -> dto.getId().equals(listId)).findAny().get();
    }

    public static ShoppingListDto singleFromDynamoModelByName(ShoppingLists dynamoLists, String listName) {
        List<ShoppingListDto> all = fromDynamoModel(dynamoLists);
        return all.stream().filter(dto -> dto.getName().toLowerCase().equals(listName.toLowerCase())).findFirst().get();
    }

    public static String toDynamoModel(List<ShoppingListDto> lists) {
        try {
            return MAPPER.writeValueAsString(lists);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not write json", e);
        }
    }

    private static <T> T fromJSON(final TypeReference<T> type, final String jsonPacket) {
        try {
            return MAPPER.readValue(jsonPacket, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read json", e);
        }
    }
}