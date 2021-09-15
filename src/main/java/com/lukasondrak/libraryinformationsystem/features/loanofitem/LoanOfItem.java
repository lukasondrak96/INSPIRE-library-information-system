package com.lukasondrak.libraryinformationsystem.features.loanofitem;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import com.lukasondrak.libraryinformationsystem.features.loan.LoanState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * One item of loan entity
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfItem {

    @Id
    @Setter(AccessLevel.PRIVATE)
    @SequenceGenerator(name = "LoanOfItemIdGenerator", sequenceName = "LOAN_OF_ITEM_SEQUENCE", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LoanOfItemIdGenerator")
    private Long idLoanOfItem;

    @Enumerated(EnumType.STRING)
    private LoanState state;

    @NotNull(message = "Chybějící čas konce výpůjčky")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "idLoan")
    private Loan loan;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "idItem")
    private Item item;

    public LoanOfItem(LoanState state, LocalDate endDate) {
        this.state = state;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanOfItem that = (LoanOfItem) o;
        return Objects.equals(idLoanOfItem, that.idLoanOfItem) && state == that.state && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLoanOfItem, state, endDate);
    }
}
