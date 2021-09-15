package com.lukasondrak.libraryinformationsystem.repositoryTests;

import com.lukasondrak.libraryinformationsystem.features.author.Author;
import com.lukasondrak.libraryinformationsystem.features.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableTransactionManagement
public class AuthorRepositoryTest {

    public static final long ID_NOT_IN_DB = 123456L;
    public static final String MOCK_NAME = "MockName";
    public static final String MOCK_SURNAME = "MockSurname";
    private static Author author;

    @Autowired
    private AuthorRepository authorRepository;


    @BeforeEach
    public void beforeEach() {
        authorRepository.deleteAll();
        author = new Author(MOCK_NAME, MOCK_SURNAME);
    }

    @AfterEach
    public void afterEach() {
        authorRepository.deleteAll();
    }

    @Test
    public void saveAuthorAndFindByIdTest() {
        Author savedAuthor = authorRepository.save(author);

        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getIdAuthor());

        assertTrue(foundAuthor.isPresent(), "We should find some author");
        assertEquals(savedAuthor, foundAuthor.get(), "We should get the same author as the author we saved");
        assertEquals(MOCK_NAME, foundAuthor.get().getName(), "We should get the same author name as the authors name we saved");
        assertEquals(MOCK_SURNAME, foundAuthor.get().getSurname(), "We should get the same author surname as the authors name we saved");

    }

    @Test
    public void findAllTest() {
        Author author2 = new Author("MockName2", "MockSurname2");
        List<Author> savedAuthorList = new ArrayList<>();
        savedAuthorList.add(authorRepository.save(author));
        savedAuthorList.add(authorRepository.save(author2));

        List<Author> allAuthors = authorRepository.findAll();

        assertFalse(allAuthors.isEmpty(), "There should be some author");
        assertEquals(2, allAuthors.size(), "There should be only two authors");
        assertEquals(allAuthors, savedAuthorList, "Saved authors should be the same as the authors we saved");
    }

    @Test
    public void findByNotExistentIdTest() {
        Optional<Author> foundAuthor = authorRepository.findById(ID_NOT_IN_DB);

        assertTrue(foundAuthor.isEmpty(), "There should not be any author with this ID");
    }

    @Test
    public void deleteTest() {
        Author savedAuthor = authorRepository.save(author);
        authorRepository.delete(savedAuthor);

        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getIdAuthor());

        assertTrue(foundAuthor.isEmpty(), "There should not be any author with this ID");
    }

    @Test
    public void deleteNotExistentAuthorTest() {
        authorRepository.delete(author);

        List<Author> allAuthors = authorRepository.findAll();

        assertTrue(allAuthors.isEmpty(), "There should not be any author");
    }

}
