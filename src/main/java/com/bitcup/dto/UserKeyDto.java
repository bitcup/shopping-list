package com.bitcup.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author bitcup
 */
public class UserKeyDto {
    private String userId;
    private String privateKey;

    public UserKeyDto() {
    }

    public UserKeyDto(String userId, String privateKey) {
        this.userId = userId;
        this.privateKey = privateKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("privateKey", privateKey)
                .toString();
    }
}
