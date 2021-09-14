package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    private LoanState state;

    @NotNull(message = "Chybějící čas začátku výpůjčky")
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    private Client clientReference;


    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanOfItem> itemsOfLoan = new ArrayList<>();

    public String translateState(LoanState state) {
        switch (state) {
            case NOT_YET_RETURNED:
                return "Půjčeno";
            case RETURNED_ON_TIME:
                return "Vráceno včas";
            case RETURNED_LATE:
                return "Vráceno po termínu";
            default:
                return "Neznámý stav";
        }
    }

    public String dateToEuropeanTimeFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd. MM. yyyy"));
    }

}
