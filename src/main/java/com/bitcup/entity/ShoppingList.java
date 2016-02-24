package com.bitcup.entity;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author bitcup
 */
@EqualsAndHashCode
@ToString
public class ShoppingList {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<ShoppingItem> items = Lists.newArrayList();
}
