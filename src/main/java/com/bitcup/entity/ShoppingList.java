package com.bitcup.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

/**
 * Represents a row in the shopping-lists Dynamo table:
 *
 * userId  listId  listName      listOrder   itemId  itemName  purchased   itemOrder
 * u1      l1      Costco        0           i1      bread     false       0
 * u1      l1      Costco        0           i2      milk      false       1
 * u1      l2      MarketBasket  1           i3      nuts      true        0
 * u1      l2      MarketBasket  1           i4      oil       true        1
 * u1      l3      Stop&Shop     2
 *
 * @author bitcup
 */
@DynamoDBTable(tableName = "shopping-lists")
public class ShoppingList {

    @Id
    private ShoppingListId shoppingListId;

    private String listName;
    private int listOrder;

    private String itemId;
    private String itemName;
    private boolean purchased;
    private int itemOrder;

    public ShoppingList() {
    }

    public ShoppingList(ShoppingListId shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return shoppingListId != null ? shoppingListId.getUserId() : null;
    }

    @DynamoDBRangeKey(attributeName = "listId")
    public String getListId() {
        return shoppingListId != null ? shoppingListId.getListId() : null;
    }

    @DynamoDBAttribute(attributeName = "listName")
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    @DynamoDBAttribute(attributeName = "listOrder")
    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }

    @DynamoDBAttribute(attributeName = "itemId")
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @DynamoDBAttribute(attributeName = "itemName")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @DynamoDBAttribute(attributeName = "itemOrder")
    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    @DynamoDBAttribute(attributeName = "purchased")
    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }


    public void setUserId(String userId) {
        if (shoppingListId == null) {
            shoppingListId = new ShoppingListId();
        }
        shoppingListId.setUserId(userId);
    }

    public void setListId(String listId) {
        if (shoppingListId == null) {
            shoppingListId = new ShoppingListId();
        }
        shoppingListId.setListId(listId);
    }

}
