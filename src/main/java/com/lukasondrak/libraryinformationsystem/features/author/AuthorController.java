package com.lukasondrak.libraryinformationsystem.features.author;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Controller
@AllArgsConstructor
public class AuthorController {

    private AuthorService authorService;

    /**
     * Returns authors page with set model
     * @param model model
     * @param session http session
     * @return authors page
     */
    @GetMapping("/authors")
    public String authorsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("authors", authorService.getAllAuthorsInfo());

        clearSessionResultAttribute(session);
        return "pages/author/authors";
    }

    /**
     * Returns add new author page with model
     * @param model model
     * @param session http session
     * @return add new author page
     */
    @GetMapping("/authors/new")
    public String newAuthorPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("author", new Author());

        clearSessionResultAttribute(session);
        return "pages/author/newAuthor";
    }

    /**
     * Delete author endpoint
     * @param id author's id
     * @param session http session
     * @return author's page
     */
    @DeleteMapping("/author/{id}")
    public String deleteAuthor(@PathVariable long id, HttpSession session) {
        return authorService.deleteAuthor(id, session);
    }

    /**
     * Create new author endpoint
     * @param newAuthor new author info
     * @param result binding result of form
     * @param session http session
     * @return authors page
     */
    @PostMapping("/authors/new")
    public String addNewAuthor(@Valid @ModelAttribute Author newAuthor, BindingResult result,
                               HttpSession session) {
        return authorService.addNewAuthor(newAuthor, result, session);
    }

}
