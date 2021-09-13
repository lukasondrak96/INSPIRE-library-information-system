package com.lukasondrak.libraryinformationsystem.features.loan;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface LoanService {

    String returnItemOfLoan(long clientId, long loanId, long itemId, HttpSession session);

    List<Loan> getAllLoans();


    void deleteLoan(Loan loan);

    void deleteLoanById(long loanId);

    String extendItemOfLoanByTwoWeeks(long clientId, long loanId, long itemId, HttpSession session);
}