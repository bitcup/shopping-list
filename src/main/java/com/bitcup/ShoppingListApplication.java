package com.bitcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties", "classpath:aws.properties"})
public class ShoppingListApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingListApplication.class, args);
    }
}
