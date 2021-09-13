package com.lukasondrak.libraryinformationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientInfoWithoutLoansDto extends PersonInfo {

    private String email;

    public ClientInfoWithoutLoansDto(Long id, String name, String surname, String email) {
        super(id, name, surname);
        this.email = email; 
    }
}
