package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.author.dto.AuthorInfoWithoutItemsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public AuthorInfoWithoutItemsDto getAuthorInfoById(long authorId) {
        Optional<Author> authorNullable = authorRepository.findById(authorId);
        if(authorNullable.isPresent()) {
            Author foundedAuthor = authorNullable.get();
            return new AuthorInfoWithoutItemsDto(authorId, foundedAuthor.getName(), foundedAuthor.getSurname());
        }
        return null;
    }

    @Override
    public List<AuthorInfoWithoutItemsDto> getAllAuthorsInfo() {
        return authorRepository.findAll()
                .stream()
                .map(author -> new AuthorInfoWithoutItemsDto(author.getIdAuthor(), author.getName(), author.getSurname()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorInfoWithoutItemsDto> getAuthorInfoByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public void deleteAuthorById(long authorId) {
        authorRepository.deleteById(authorId);
    }
}
