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
    public Optional<Author> findById(long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public List<AuthorInfoWithoutItemsDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(author -> new AuthorInfoWithoutItemsDto(author.getIdAuthor(), author.getName(), author.getSurname()))
                .collect(Collectors.toList());
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
