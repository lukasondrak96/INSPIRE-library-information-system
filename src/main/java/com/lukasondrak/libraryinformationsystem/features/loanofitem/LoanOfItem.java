package com.lukasondrak.libraryinformationsystem.features.loanofitem;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import com.lukasondrak.libraryinformationsystem.features.loan.LoanState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idLoan")
    private Loan loan;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "idItem")
    private Item item;

}
