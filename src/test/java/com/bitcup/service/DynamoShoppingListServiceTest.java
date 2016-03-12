package com.bitcup.service;

import com.bitcup.ShoppingListApplication;
import com.bitcup.dto.ShoppingListDto;
import com.bitcup.repository.ShoppingListsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    private final static String OWNER = "abc";

    @Autowired
    private DynamoShoppingListService service;

    @Autowired
    private ShoppingListsRepository repository;

    @Before
    public void cleanup() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void testCRUD() throws Exception {
        List<ShoppingListDto> all = service.getAllLists(OWNER);
        Assert.assertEquals(0, all.size());

        all = service.addList(OWNER, "list1");
        Assert.assertEquals(1, all.size());

        all = service.getAllLists(OWNER);
        Assert.assertEquals(1, all.size());

        String list1Id = all.get(0).getId();
        ShoppingListDto list1 = service.getList(OWNER, list1Id);
        Assert.assertEquals(list1Id, list1.getId());

        list1 = service.addItemToList(OWNER, list1Id, "item1");
        Assert.assertEquals(1, list1.getItems().size());
        Assert.assertEquals("item1", list1.getItems().get(0).getName());

        list1 = service.addItemToList(OWNER, list1Id, "item2");
        Assert.assertEquals(2, list1.getItems().size());
        Assert.assertEquals("item1", list1.getItems().get(0).getName());
        Assert.assertEquals("item2", list1.getItems().get(1).getName());

        all = service.deleteList(OWNER, list1Id);
        Assert.assertEquals(0, all.size());

        all = service.getAllLists(OWNER);
        Assert.assertEquals(0, all.size());
    }
}
