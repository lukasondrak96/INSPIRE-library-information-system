package com.lukasondrak.libraryinformationsystem.features.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Controller
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @GetMapping("/items")
    public String itemsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("items", itemService.getAllItems());

        clearSessionResultAttribute(session);
        return "pages/item/items";
    }

    @GetMapping("/items/newItem")
    public String newItemPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        Item item = new Item();
        item.setYearOfPublication(2021);
        model.addAttribute("item", item);

        clearSessionResultAttribute(session);
        return "pages/item/newItem";
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable long id, HttpSession session) {
        return itemService.deleteItem(id, session);
    }

    @PostMapping("/item/{idItem}/addAuthor")
    public String addAuthorToItem(@PathVariable long idItem, @NotBlank @RequestParam("author") String author, HttpSession session) {
        return itemService.addAuthorToItem(idItem, author, session);
    }

    @PostMapping("/items/newItem")
    public String addNewItem(@Valid @ModelAttribute Item newItem, BindingResult result, @NotBlank @RequestParam("author") String author,
                               HttpSession session) {
        return itemService.addNewItem(newItem, result, author, session);
    }

    @GetMapping("/item/{idItem}/addAuthor")
    public String addAuthorPage(@PathVariable long idItem, Model model, HttpSession session) {
        return itemService.prepareAddAuthorToItemPage(idItem, model, session);
    }

}
