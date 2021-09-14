package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.dto.AuthorInfoWithoutItemsDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorInfoWithoutItemsDto> getAllAuthorsInfo() {
        return authorRepository.findAll()
                .stream()
                .map(author -> new AuthorInfoWithoutItemsDto(author.getIdAuthor(), author.getName(), author.getSurname()))
                .collect(Collectors.toList());
    }

    @Override
    public String addNewAuthor(Author newAuthor, BindingResult result, HttpSession session) {
        if(result.hasErrors()) {
            LOGGER.error("Validation error while adding new author");
            return "pages/author/newAuthor";
        }

        if(getAuthorByNameAndSurname(newAuthor.getName(), newAuthor.getSurname()).isPresent()) {
            LOGGER.error("Error while creating author, author is not in database");
            session.setAttribute("result", "Nepodařilo se vytvořit autora. Autor se již nachází v databázi.");
            return "redirect:/authors/new";
        }

        authorRepository.save(new Author(newAuthor.getName(), newAuthor.getSurname()));

        LOGGER.debug("Author " + newAuthor.getName() + " " + newAuthor.getSurname() + " successfully created");
        session.setAttribute("result", "Autor " + newAuthor.getName() + " " + newAuthor.getSurname() + " byl úspěšně vytvořen.");
        return "redirect:/authors";
    }

    @Override
    public String deleteAuthor(long id, HttpSession session) {
        List<AuthorInfoWithoutItemsDto> allAuthors = getAllAuthorsInfo();
        session.setAttribute("authors", allAuthors);

        Optional<Author> authorToDelete = findById(id);
        if(authorToDelete.isEmpty()) {
            LOGGER.error("Error while deleting author, author is not in database");
            session.setAttribute("result", "Nepodařilo se smazat autora. Autor se nenachází v databázi.");
        } else {
            try {
                authorRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                LOGGER.error("Error while deleting author, author has items");
                session.setAttribute("result", "Nepodařilo se smazat autora. Autor má položky v databázi.");
                return "redirect:/authors";
            }
            Author deletedAuthor = authorToDelete.get();
            LOGGER.debug("Author " + deletedAuthor.getName() + " " + deletedAuthor.getSurname() + " successfully deleted");
            session.setAttribute("result", "Autor " + deletedAuthor.getName() + " " + deletedAuthor.getSurname() + " byl úspěšně vymazán.");
        }
        return "redirect:/authors";
    }

    private Optional<Author> getAuthorByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    private Optional<Author> findById(long authorId) {
        return authorRepository.findById(authorId);
    }

}
