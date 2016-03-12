package com.bitcup.repository;

import com.bitcup.entity.ShoppingLists;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bitcup
 */
@Repository
public interface ShoppingListsRepository extends CrudRepository<ShoppingLists, String> {

    ShoppingLists findByUserId(String userId);

    @EnableScan
    void deleteAll();

    @EnableScan
    Iterable<ShoppingLists> findAll();
}
