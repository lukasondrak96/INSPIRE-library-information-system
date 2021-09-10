package com.lukasondrak.libraryinformationsystem.features.loanofitem;

import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import com.lukasondrak.libraryinformationsystem.features.loan.LoanState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfItem {

    @Id
    private Long idLoanOfItem;

    @Enumerated(EnumType.STRING)
    private LoanState state;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "idLoan")
    private Loan loan;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "idItem")
    private Item item;

}
