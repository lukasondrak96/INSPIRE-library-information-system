package com.lukasondrak.libraryinformationsystem.repositoryTests;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.item.ItemRepository;
import com.lukasondrak.libraryinformationsystem.features.item.ItemType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableTransactionManagement
public class ItemRepositoryTest {

    public static final long ID_NOT_IN_DB = 123456L;
    public static final String MOCK_TITLE = "MockTitle";
    public static final int MOCK_YEAR = 2020;
    public static final ItemType MOCK_ITEM_TYPE = ItemType.CD;

    private static Item item;

    @Autowired
    private ItemRepository itemRepository;


    @BeforeEach
    public void beforeEach() {
        itemRepository.deleteAll();
        item = new Item(MOCK_TITLE, MOCK_YEAR, MOCK_ITEM_TYPE);
    }

    @AfterEach
    public void afterEach() {
        itemRepository.deleteAll();
    }

    @Test
    public void saveItemAndFindByIdTest() {
        Item savedItem = itemRepository.save(item);

        Optional<Item> foundItem = itemRepository.findById(savedItem.getIdItem());

        assertTrue(foundItem.isPresent(), "We should find some item");
        assertEquals(savedItem, foundItem.get(), "We should get the same item as the item we saved");
        assertEquals(MOCK_TITLE, foundItem.get().getTitle(), "We should get the same items title the items title we saved");
        assertEquals(MOCK_ITEM_TYPE, foundItem.get().getType(), "We should get the same item type as the items type we saved");
        assertEquals(MOCK_YEAR, foundItem.get().getYearOfPublication(), "We should get the same item year of publication as the items year we saved");

    }

    @Test
    public void findAllTest() {
        Item item2 = new Item(MOCK_TITLE + "2", MOCK_YEAR - 20, MOCK_ITEM_TYPE);
        List<Item> savedItemsList = new ArrayList<>();
        savedItemsList.add(itemRepository.save(item));
        savedItemsList.add(itemRepository.save(item2));

        List<Item> allItems = itemRepository.findAll();

        assertFalse(allItems.isEmpty(), "There should be some item");
        assertEquals(2, allItems.size(), "There should be only two items");
        assertEquals(allItems, savedItemsList, "Saved items should be the same as the items we saved");
    }

    @Test
    public void findByNotExistentIdTest() {
        Optional<Item> foundItem = itemRepository.findById(ID_NOT_IN_DB);

        assertTrue(foundItem.isEmpty(), "There should not be any item with this ID");
    }

    @Test
    public void deleteTest() {
        Item savedItem = itemRepository.save(item);
        itemRepository.delete(savedItem);

        Optional<Item> foundItem = itemRepository.findById(savedItem.getIdItem());

        assertTrue(foundItem.isEmpty(), "There should not be any item with this ID");
    }

    @Test
    public void deleteNotExistentItemTest() {
        itemRepository.delete(item);

        List<Item> allItems = itemRepository.findAll();

        assertTrue(allItems.isEmpty(), "There should not be any item");
    }

}
