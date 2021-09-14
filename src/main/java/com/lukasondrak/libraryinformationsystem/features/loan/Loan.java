package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @NotEmpty(message = "Nelze vytvořit výpůjčku bez položek")
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanOfItem> itemsOfLoan = new ArrayList<>();

    public String dateToEuropeanTimeFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd. MM. yyyy"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(idLoan, loan.idLoan) && state == loan.state && Objects.equals(startDate, loan.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLoan, state, startDate);
    }
}
