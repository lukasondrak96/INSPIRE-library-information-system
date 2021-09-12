package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.author.dto.AuthorInfoWithoutItemsDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorInfoWithoutItemsDto getAuthorInfoById(long authorId);

    List<AuthorInfoWithoutItemsDto> getAllAuthorsInfo();

    Optional<AuthorInfoWithoutItemsDto> getAuthorInfoByNameAndSurname(String name, String surname);


    Author save(Author author);

    void deleteAuthor(Author author);

    @Transactional
    void deleteAuthorById(long authorId);
}

