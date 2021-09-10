package com.lukasondrak.libraryinformationsystem.features.client;

import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client implements Serializable {

    @Id
    @SequenceGenerator(name = "ClientIdGenerator", sequenceName = "CLIENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientIdGenerator")
    private Long idClient;


    private String name;
    private String surname;
    private String email;

    @OneToMany(mappedBy = "clientReference", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Loan> clientLoans = new ArrayList<>();;

}
