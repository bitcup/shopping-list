package com.bitcup.repository;

import com.bitcup.entity.ShoppingList;
import com.bitcup.entity.ShoppingListId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bitcup
 */
@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingList, ShoppingListId> {
    List<ShoppingList> findAllByUserId(String userId);
    List<ShoppingList> findAllByUserIdAndListId(String userId, String listId);
}
