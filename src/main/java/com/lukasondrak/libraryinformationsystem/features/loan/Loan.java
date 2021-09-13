package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @SequenceGenerator(name = "LoanIdGenerator", sequenceName = "LOAN_SEQUENCE", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LoanIdGenerator")
    private Long idLoan;

    @Enumerated(EnumType.STRING)
    @Size(max = 255, message = "Příliš dlouhý vstup")
    private LoanState state;

    @NotNull(message = "Chybějící čas začátku vypůjčky")
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    private Client clientReference;


    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<LoanOfItem> itemsOfLoan = new ArrayList<>();

    public String translateState(LoanState state) {
        switch (state) {
            case ONGOING:
                return "Probíhající";
            case RETURNED_IN_DUE_TIME:
                return "Vráceno včas";
            case NOT_RETURNED_IN_DUE_TIME:
                return "Nevráceno včas";
            default:
                return "Neznámý stav";
        }
    }

    public String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
