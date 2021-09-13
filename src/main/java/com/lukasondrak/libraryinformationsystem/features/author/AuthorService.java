package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.dto.AuthorInfoWithoutItemsDto;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorInfoWithoutItemsDto> getAllAuthorsInfo();

    String addNewAuthor(Author newAuthor, BindingResult result, HttpSession session);

    String deleteAuthor(long id, HttpSession session);
}

