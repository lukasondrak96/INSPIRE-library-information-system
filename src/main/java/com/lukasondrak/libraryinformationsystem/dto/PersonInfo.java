package com.lukasondrak.libraryinformationsystem.dto;

import lombok.Data;

@Data
public abstract class PersonInfo {
    private Long id;
    private String name;
    private String surname;

    public PersonInfo() {
    }

    public PersonInfo(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
}
