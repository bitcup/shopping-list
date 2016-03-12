package com.bitcup.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

/**
 * @author bitcup
 */
@DynamoDBTable(tableName = "shopping-lists")
public class ShoppingLists {
    @Id
    private String userId;

    private String listsJson;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "listsJson")
    public String getListsJson() {
        return listsJson;
    }

    public void setListsJson(String listsJson) {
        this.listsJson = listsJson;
    }
}
