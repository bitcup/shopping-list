package com.bitcup.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author bitcup
 */
@EqualsAndHashCode
@ToString
public class ShoppingItem {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private boolean purchased;
}
