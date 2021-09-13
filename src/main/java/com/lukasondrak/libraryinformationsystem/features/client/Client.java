package com.lukasondrak.libraryinformationsystem.features.client;

import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Client implements Serializable {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "ClientIdGenerator", sequenceName = "CLIENT_SEQUENCE", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientIdGenerator")
    private Long idClient;

    @NotBlank(message = "Prosím vyplňte jméno klienta")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String name;


    @NotBlank(message = "Prosím vyplňte příjmení klienta")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private String surname;

    @NotBlank(message = "Prosím vyplňte e-mailovou adresu klienta")
    @Email(message = "Zkontrolujte, zda je e-mail ve správném formátu")
    @Size(max = 255, message = "Příliš dlouhý vstup")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "clientReference", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Loan> clientLoans = new ArrayList<>();

    public Client(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
