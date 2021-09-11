package com.lukasondrak.libraryinformationsystem.features.item;

import com.lukasondrak.libraryinformationsystem.features.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<Item> findById(long itemId);

    List<Item> getAllItems();


    Item save(Item item);

    void deleteItem(Item item);

    void deleteItemById(long itemId);
}