package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.dto.AuthorInfoWithoutItemsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

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
            return "pages/author/newAuthor";
        }

        if(getAuthorByNameAndSurname(newAuthor.getName(), newAuthor.getSurname()).isPresent()) {
            session.setAttribute("result", "Nepodařilo se vytvořit autora. Autor se již nachází v databázi.");
            return "redirect:/authors/new";
        }

        Author savedAuthor = save(new Author(newAuthor.getName(), newAuthor.getSurname()));

        session.setAttribute("result", "Autor " + newAuthor.getName() + " " + newAuthor.getSurname() + " byl úspěšně vytvořen.");
        return "redirect:/authors";
    }

    @Override
    public String deleteAuthor(long id, HttpSession session) {
        List<AuthorInfoWithoutItemsDto> allAuthors = getAllAuthorsInfo();
        session.setAttribute("authors", allAuthors);

        Optional<Author> authorToDelete = findById(id);
        if(authorToDelete.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se smazat autora. Autor se nenachází v databázi.");
        } else {
            deleteAuthorById(id);
            Author deletedAuthor = authorToDelete.get();
            session.setAttribute("result", "Autor " + deletedAuthor.getName() + " " + deletedAuthor.getSurname() + " byl úspěšně vymazán.");
        }
        return "redirect:/authors";
    }

    @Transactional
    protected void deleteAuthorById(long authorId) {
        authorRepository.deleteById(authorId);
    }


    private Author save(Author author) {
        return authorRepository.save(author);
    }

    private Optional<Author> getAuthorByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    private Optional<Author> findById(long authorId) {
        return authorRepository.findById(authorId);
    }

}
