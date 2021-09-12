package com.lukasondrak.libraryinformationsystem.features.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorInfoWithoutItemsDto {

    private Long idAuthor;
    private String name;
    private String surname;
}
