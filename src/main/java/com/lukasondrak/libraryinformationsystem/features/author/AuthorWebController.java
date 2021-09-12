package com.lukasondrak.libraryinformationsystem.features.author;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.lukasondrak.libraryinformationsystem.features.author.dto.AuthorInfoWithoutItemsDto;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Controller
@AllArgsConstructor
public class AuthorWebController {

    private AuthorService authorService;

    @GetMapping("/authors")
    public String authorsPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("authors", authorService.getAllAuthorsInfo());

        clearSessionResultAttribute(session);
        return "pages/authors";
    }

    @GetMapping("/authors/new")
    public String newAuthorPage(Model model, HttpSession session) {
        model.addAttribute("result",session.getAttribute("result"));
        model.addAttribute("author", new Author());

        clearSessionResultAttribute(session);
        return "pages/newAuthor";
    }

    @DeleteMapping("/authors/delete")
    public String deleteAuthor(@ModelAttribute(value="author") AuthorInfoWithoutItemsDto author, HttpSession session) {
        session.setAttribute("authors", authorService.getAllAuthorsInfo());

        try {
            authorService.deleteAuthorById(author.getIdAuthor());
        } catch (EmptyResultDataAccessException e) {
            session.setAttribute("result", "Nepodařilo se smazat autora. Autor se nenachází v databázi.");
            return "redirect:/authors";
        }

        session.setAttribute("result", "Autor " + author.getName() + " " + author.getSurname() + " byl úspěšně vymazán.");
        return "redirect:/authors";
    }



    @PostMapping("/authors/new")
    public String addNewAuthor(@RequestParam("name") String name,
                               @RequestParam("surname") String surname,
                               HttpSession session) {
        if(authorService.getAuthorInfoByNameAndSurname(name, surname).isPresent()) {
            session.setAttribute("result", "Nepodařilo se vytvořit autora. Autor se již nachází v databázi.");
            return "redirect:/authors/new";
        }

        Author savedAuthor = authorService.save(new Author(name, surname));
        if(savedAuthor == null) {
            session.setAttribute("result", "Nepodařilo se vytvořit autora, zkuste to znovu později.");
            return "redirect:/authors/new";
        }

        session.setAttribute("result", "Autor " + name + " " + surname + " byl úspěšně vytvořen.");
        return "redirect:/authors";
    }

}
