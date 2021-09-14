package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.dto.AuthorInfoWithoutItemsDto;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

public interface AuthorService {

    List<AuthorInfoWithoutItemsDto> getAllAuthorsInfo();

    String addNewAuthor(Author newAuthor, BindingResult result, HttpSession session);

    @Transactional
    String deleteAuthor(long id, HttpSession session);
}

