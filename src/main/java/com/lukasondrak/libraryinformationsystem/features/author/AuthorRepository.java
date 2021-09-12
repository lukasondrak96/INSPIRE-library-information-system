package com.lukasondrak.libraryinformationsystem.features.author;

import com.lukasondrak.libraryinformationsystem.features.author.dto.AuthorInfoWithoutItemsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<AuthorInfoWithoutItemsDto> findByNameAndSurname(String name, String surname);
}
