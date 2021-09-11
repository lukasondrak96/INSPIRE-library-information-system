package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.author.dto.AuthorInfoWithoutItemsDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Optional<Author> findById(long authorId);

    List<AuthorInfoWithoutItemsDto> getAllAuthors();


    Author save(Author author);

    void deleteAuthor(Author author);

    void deleteAuthorById(long authorId);
}

