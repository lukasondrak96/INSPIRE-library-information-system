package com.lukasondrak.libraryinformationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorInfoWithoutItemsDto  extends PersonInfo {
    public AuthorInfoWithoutItemsDto(Long id, String name, String surname) {
        super(id, name, surname);
    }
}
