package com.lukasondrak.libraryinformationsystem.features.loan;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class LoanController {

    private LoanService loanService;

    @GetMapping("client/{clientId}/loans/new")
    public String newClientsLoanPage(@PathVariable long clientId, Model model, HttpSession session) {
        return loanService.prepareNewClientsLoanPage(clientId, model, session);
    }

    @GetMapping("client/{clientId}/loan/{loanId}/return")
    public String returnAllItemsOfLoan(@PathVariable long clientId, @PathVariable long loanId, HttpSession session) {

        return loanService.returnAllItemsOfLoan(clientId, loanId, session);
    }

    @GetMapping("client/{clientId}/loan/{loanId}/item/{itemId}/return")
    public String returnItemOfLoan(@PathVariable long clientId, @PathVariable long loanId, @PathVariable long itemId, HttpSession session) {

        return loanService.returnItemOfLoan(clientId, loanId, itemId, session);
    }

    @GetMapping("client/{clientId}/loan/{loanId}/item/{itemId}/extend")
    public String extendItemOfLoanByTwoWeeks(
            @PathVariable long clientId, @PathVariable long loanId, @PathVariable long itemId, HttpSession session
    ) {
        return loanService.extendItemOfLoanByTwoWeeks(clientId, loanId, itemId, session);
    }

    @DeleteMapping("client/{clientId}/loan/{loanId}/item/{itemId}")
    public String deleteItemOfLoanOfClient(
            @PathVariable long clientId, @PathVariable long loanId, @PathVariable long itemId, HttpSession session
    ) {
        return loanService.deleteItemOfLoanOfClient(clientId, loanId, itemId, session);
    }


    @DeleteMapping("client/{clientId}/loan/{loanId}")
    public String deleteLoanOfClient(@PathVariable long clientId, @PathVariable long loanId, HttpSession session) {
        return loanService.deleteLoanOfClient(loanId, clientId, session);
    }

    @PostMapping("client/{clientId}/loans/new")
    public String addNewLoanToClient(@Valid @ModelAttribute Loan newLoan, BindingResult result, @PathVariable long clientId,
                                     HttpSession session) {
        return loanService.addNewLoanToClient(newLoan, result, clientId, session);
    }


}
