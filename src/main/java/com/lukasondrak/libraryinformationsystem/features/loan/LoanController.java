package com.lukasondrak.libraryinformationsystem.features.loan;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class LoanController {

    private LoanService loanService;

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


}
