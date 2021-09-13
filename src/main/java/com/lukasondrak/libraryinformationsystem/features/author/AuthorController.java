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

    @GetMapping("/authors")
    public String authorsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("authors", authorService.getAllAuthorsInfo());

        clearSessionResultAttribute(session);
        return "pages/author/authors";
    }

    @GetMapping("/authors/new")
    public String newAuthorPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("author", new Author());

        clearSessionResultAttribute(session);
        return "pages/author/newAuthor";
    }

    @DeleteMapping("/authors/{id}")
    public String deleteAuthor(@PathVariable long id, HttpSession session) {
        return authorService.deleteAuthor(id, session);
    }

    @PostMapping("/authors/new")
    public String addNewAuthor(@Valid @ModelAttribute Author newAuthor, BindingResult result,
                               HttpSession session) {
        return authorService.addNewAuthor(newAuthor, result, session);
    }

}
