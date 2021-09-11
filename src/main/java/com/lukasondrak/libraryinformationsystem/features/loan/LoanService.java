package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.loan.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Optional<Loan> findById(long loanId);

    List<Loan> getAllLoans();


    Loan save(Loan loan);

    void deleteLoan(Loan loan);

    void deleteLoanById(long loanId);
}