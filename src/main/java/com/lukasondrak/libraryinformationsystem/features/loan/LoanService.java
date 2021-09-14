package com.lukasondrak.libraryinformationsystem.features.loan;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface LoanService {

    String returnItemOfLoan(long clientId, long loanId, long itemId, HttpSession session);

    String deleteLoanOfClient(long loanId, long clientId, HttpSession session);

    String extendItemOfLoanByTwoWeeks(long clientId, long loanId, long itemId, HttpSession session);

    String returnAllItemsOfLoan(long clientId, long loanId, HttpSession session);

    String prepareNewClientsLoanPage(long clientId, Model model, HttpSession session);

    String deleteItemOfLoanOfClient(long clientId, long loanId, long itemId, HttpSession session);

    String addNewLoanToClient(long[] itemIdsToBorrow, long clientId, HttpSession session);
}