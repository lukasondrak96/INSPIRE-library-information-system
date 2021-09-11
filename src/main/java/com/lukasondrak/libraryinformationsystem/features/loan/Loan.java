package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "LoanIdGenerator", sequenceName = "LOAN_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LoanIdGenerator")
    private Long idLoan;

    @Enumerated(EnumType.STRING)
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private LoanState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    private Client clientReference;


    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<LoanOfItem> itemsOfLoan = new ArrayList<>();

}