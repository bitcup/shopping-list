package com.bitcup.service;

import com.bitcup.ShoppingListApplication;
import com.bitcup.dto.ShoppingListDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author bitcup
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShoppingListApplication.class)
@Component
public class DynamoShoppingListServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoShoppingListServiceTest.class);

    @Autowired
    private DynamoShoppingListService service;

    @Test
    public void testCRUD() throws Exception {
        List<ShoppingListDto> all = service.getAllLists("abc");
        LOGGER.info("all: {}", all);

        service.addList("abc", "list1");
        LOGGER.info("list1 added");

        all = service.getAllLists("abc");
        LOGGER.info("after add, all: {}", all);

        String list1Id = all.get(0).getId();
        ShoppingListDto fromDB = service.getList("abc", list1Id);
        LOGGER.info("by id, fromDB: {}", fromDB);

        service.deleteList("abc", list1Id);
        LOGGER.info("deleted list1 with id: {}", list1Id);

        all = service.getAllLists("abc");
        LOGGER.info("after delete, all: {}", all);
    }


//    @Test
//    public void testCreate() throws Exception {
//        //ShoppingUser user = service.createUser("abc");
//        ShoppingUser user = new ShoppingUser();
//        user.setUserId("abc");
//
//        ShoppingList list1 = createList("l1", "list1");
//        list1.setItems(Lists.newArrayList(createItem("i1", "item1", false), createItem("i2", "item2", true)));
//
//        user.setListsJson(JSON_MAPPER.writeValueAsString(list1));
//
//        service.addList(user.getUserId(), list1);
//
//

//        ShoppingList list2 = createList("l2", "list2");
//        list2.setItems(Lists.newArrayList(createItem("i3", "item3", false)));
//
//        List<ShoppingList> lists = Lists.newArrayList(list1, list2);
//        ShoppingUser user = new ShoppingUser();
//        user.setUserId("u1");
//        String listsJson = JSON_MAPPER.writeValueAsString(lists);
//        LOGGER.info(listsJson);
//        user.setListsJson(listsJson);
//
//        service.addList(user.getUserId(), list1);
//    }

//    private ShoppingItem createItem(String id, String name, boolean purchased) {
//        ShoppingItem item = new ShoppingItem();
//        item.setId(id);
//        item.setName(name);
//        item.setPurchased(purchased);
//        return item;
//    }
//
//    private ShoppingList createList(String id, String name) {
//        ShoppingList list = new ShoppingList();
//        list.setId(id);
//        list.setName(name);
//        return list;
//    }

}
