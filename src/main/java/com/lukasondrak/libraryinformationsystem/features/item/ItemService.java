package com.lukasondrak.libraryinformationsystem.features.item;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    String deleteItem(long itemId, HttpSession session);

    List<Item> getAllItems();

    String prepareAddAuthorToItemPage(long idItem, Model model, HttpSession session);

    String addAuthorToItem(long idItem, String author, HttpSession session);

    String addNewItem(Item newItem, BindingResult result, String author, HttpSession session);

    Optional<Item> findById(long itemId);
}