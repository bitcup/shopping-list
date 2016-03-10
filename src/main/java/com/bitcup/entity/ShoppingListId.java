package com.bitcup.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Composite Key to support a HASH+RANGE key of DynamoDB in Spring-Data
 *
 * @author bitcup
 */

public class ShoppingListId implements Serializable {

    private static final long serialVersionUID = 3792364453989621127L;

    private String userId;

    private String listId;

    @DynamoDBHashKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey
    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShoppingListId that = (ShoppingListId) o;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(listId, that.listId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(listId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("listId", listId)
                .toString();
    }
}
