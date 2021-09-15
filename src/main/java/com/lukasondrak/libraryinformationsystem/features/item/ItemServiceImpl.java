package com.lukasondrak.libraryinformationsystem.features.item;

import com.lukasondrak.libraryinformationsystem.features.author.Author;
import com.lukasondrak.libraryinformationsystem.features.author.AuthorRepository;
import com.lukasondrak.libraryinformationsystem.features.client.ClientServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ItemRepository itemRepository;
    private final AuthorRepository authorRepository;

    /**
     * Deletes item with id
     * @param itemId id of item
     * @param session http session
     * @return items page
     */
    @Override
    public String deleteItem(long itemId, HttpSession session) {
        List<Item> allItems = itemRepository.findAll();
        session.setAttribute("items", allItems);

        Optional<Item> itemToDeleteOptional = itemRepository.findById(itemId);
        if(itemToDeleteOptional.isEmpty()) {
            LOGGER.error("Error while deleting item, item is not in database");
            session.setAttribute("result", "Nepodařilo se smazat položku. Položka se nenachází v databázi.");
        } else {
            itemRepository.deleteById(itemId);
            Item deletedItem = itemToDeleteOptional.get();
            LOGGER.debug("Item " + deletedItem.getTitle() + " successfully deleted");
            session.setAttribute("result", "Item " + deletedItem.getTitle() + " byl úspěšně vymazán.");
        }
        return "redirect:/items";
    }

    /**
     * Returns all items
     * @return list of all items
     */
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * Returns page for adding author to item
     * @param idItem id of item
     * @param model model
     * @param session http session
     * @return page for adding author to item
     */
    @Override
    public String prepareAddAuthorToItemPage(long idItem, Model model, HttpSession session) {
        Optional<Item> itemOptional = itemRepository.findById(idItem);
        if (itemOptional.isEmpty()) {
            LOGGER.error("Item " + idItem + " does not exists");
            session.setAttribute("result", "Položka s id " + idItem + " nebyla nalezena.");
            return "redirect:/items";
        }

        List<Author> allAuthors = authorRepository.findAll();
        if (allAuthors.isEmpty()) {
            LOGGER.debug("There are no authors in database");
            session.setAttribute("result", "Nelze přiřadit položce autora. V databázi se nenachází žádný autor.");
            return "redirect:/items";
        }
        //only authors that are not already authors of item
        List<Author> filteredAuthors = allAuthors.stream()
                .filter(
                        author -> !itemOptional.get().getItemAuthors().contains(author)
                )
                .collect(Collectors.toList());
        if (filteredAuthors.isEmpty()) {
            LOGGER.debug("Item " + itemOptional.get().getTitle() +  " has all authors already");
            session.setAttribute("result", "Nelze přiřadit položce autora. Položka již má všechny autory.");
            return "redirect:/items";
        }

        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("authors", filteredAuthors);
        model.addAttribute("idItem", idItem);

        clearSessionResultAttribute(session);
        return "pages/item/addAuthorToItem";
    }

    /**
     * Adds author to item if its not author of this item yet
     * @param idItem id of item
     * @param author author's name(s) and surname
     * @param session http session
     * @return items page
     */
    @Override
    public String addAuthorToItem(long idItem, String author, HttpSession session) {
        Optional<Item> itemOptional = itemRepository.findById(idItem);
        if (itemOptional.isEmpty()) {
            LOGGER.error("Item with id " + idItem + " is not in database");
            session.setAttribute("result", "Autor nemůže být přiřazen položce. Položka s tímto id není v databázi");
            return "redirect:/items";
        }

        Item foundedItem = itemOptional.get();

        int lastSpaceInAuthorsFullName = author.lastIndexOf(' ');
        if (authorFullNameHasNoSpace(author, session, lastSpaceInAuthorsFullName, "Nelze přiřadit položce autora. V databázi se tento autor nenachází."))
            return "redirect:/items";

        String names = author.substring(0, lastSpaceInAuthorsFullName);
        String surname = author.substring(lastSpaceInAuthorsFullName + 1);
        Optional<Author> authorOptional = authorRepository.findByNameAndSurname(names, surname);
        if (authorIsInDatabase(authorOptional)) {
            Author foundedAuthor = authorOptional.get();
            if (foundedItem.getItemAuthors().contains(foundedAuthor)) {
                LOGGER.error("Author " + author + " is author of item " + foundedItem + " already");
                session.setAttribute("result", "Autor se jménem " + names + " " + surname + " již je autorem této položky.");
                return "redirect:/items";
            }
            foundedItem.getItemAuthors().add(foundedAuthor);
        } else {
            foundedItem.getItemAuthors().add(new Author(names, surname));
        }

        itemRepository.save(foundedItem);
        LOGGER.debug("Author " + author + " was successfully added to item " + foundedItem);
        session.setAttribute("result", "Autor se jménem " + names + " " + surname + " byl úspěšně přiřazen položce " + foundedItem.getTitle() + ".");
        return "redirect:/items";
    }

    /**
     * Creates new item
     * @param newItem new item info
     * @param result binding result of form
     * @param author author's name(s) and surname
     * @param session http session
     * @return items page
     */
    @Override
    public String addNewItem(Item newItem, BindingResult result, String author, HttpSession session) {
        if(result.hasErrors()) {
            LOGGER.error("Validation error while adding new item");
            return "pages/item/newItem";
        }

        int lastSpaceInAuthorsFullName = author.lastIndexOf(' ');
        if (authorFullNameHasNoSpace(author, session, lastSpaceInAuthorsFullName, "Nelze vytvořit položku. V databázi se tento autor nenachází."))
            return "redirect:/items";
        String names = author.substring(0, lastSpaceInAuthorsFullName);
        String surname = author.substring(lastSpaceInAuthorsFullName + 1);
        Optional<Author> authorOptional = authorRepository.findByNameAndSurname(names, surname);
        if (authorIsInDatabase(authorOptional)) {
            newItem.getItemAuthors().add(authorOptional.get());
        } else {
            newItem.getItemAuthors().add(new Author(names, surname));
        }


        itemRepository.save(newItem);

        LOGGER.debug("Item was successfully created");
        session.setAttribute("result", "Položka " + newItem.getTitle() + " byla úspěšně vytvořena.");
        return "redirect:/items";
    }

    /**
     * Finds item by id
     * @param itemId id of item
     * @return item optional
     */
    @Override
    public Optional<Item> findById(long itemId) {
        return itemRepository.findById(itemId);
    }


    private boolean authorIsInDatabase(Optional<Author> authorOptional) {
        return authorOptional.isPresent();
    }

    private boolean authorFullNameHasNoSpace(String author, HttpSession session, int lastSpaceInAuthorsFullName, String resultMessage) {
        if (lastSpaceInAuthorsFullName == -1) {
            LOGGER.error("Author " + author + " is not in database");
            session.setAttribute("result", resultMessage);
            return true;
        }
        return false;
    }

}
