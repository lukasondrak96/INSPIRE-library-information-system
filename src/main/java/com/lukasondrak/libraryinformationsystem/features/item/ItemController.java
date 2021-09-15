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

    /**
     * Returns items page with set model
     * @param model model
     * @param session http session
     * @return items page
     */
    @GetMapping("/items")
    public String itemsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("items", itemService.getAllItems());

        clearSessionResultAttribute(session);
        return "pages/item/items";
    }

    /**
     * Returns add new item page with model
     * @param model model
     * @param session http session
     * @return add new item page
     */
    @GetMapping("/items/newItem")
    public String newItemPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        Item item = new Item();
        item.setYearOfPublication(2021);
        model.addAttribute("item", item);

        clearSessionResultAttribute(session);
        return "pages/item/newItem";
    }

    /**
     * Delete item endpoint
     * @param id item's id
     * @param session http session
     * @return items page
     */
    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable long id, HttpSession session) {
        return itemService.deleteItem(id, session);
    }

    /**
     * Adds author to item
     * @param idItem id of item
     * @param author name(s) and surname of author
     * @param session http session
     * @return items page
     */
    @PostMapping("/item/{idItem}/addAuthor")
    public String addAuthorToItem(@PathVariable long idItem, @NotBlank @RequestParam("author") String author, HttpSession session) {
        return itemService.addAuthorToItem(idItem, author, session);
    }

    /**
     * Create new item endpoint
     * @param newItem new item info
     * @param result binding result of form
     * @param session http session
     * @return items page
     */
    @PostMapping("/items/newItem")
    public String addNewItem(@Valid @ModelAttribute Item newItem, BindingResult result, @NotBlank @RequestParam("author") String author,
                               HttpSession session) {
        return itemService.addNewItem(newItem, result, author, session);
    }

    /**
     * Returns page for adding author to item
     * @param idItem id of item
     * @param model model
     * @param session http session
     * @return add author to item page
     */
    @GetMapping("/item/{idItem}/addAuthor")
    public String addAuthorPage(@PathVariable long idItem, Model model, HttpSession session) {
        return itemService.prepareAddAuthorToItemPage(idItem, model, session);
    }

}
