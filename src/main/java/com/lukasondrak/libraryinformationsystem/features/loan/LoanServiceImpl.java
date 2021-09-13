package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;
    private LoanOfItemRepository loanOfItemRepository;


    @Override
    public String extendItemOfLoanByTwoWeeks(long clientId, long loanId, long itemId, HttpSession session) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se prodloužit položku, nejspíše již byla vrácena.");
            return "redirect:/client/" + clientId + "/loans";
        }

        Loan loan = loanOptional.get();
        List<LoanOfItem> loansWithThisItem =
                loan.getItemsOfLoan()
                        .stream()
                        .filter(
                                itemOfLoan -> itemOfLoan.getItem().getIdItem() == itemId)
                        .collect(Collectors.toList());
        if (loansWithThisItem.size() > 1) {
            session.setAttribute("result", "Nepodařilo se prodloužit položku, nejspíše již byla vrácena.");
            return "redirect:/client/" + clientId + "/loans";
        } else if (loansWithThisItem.size() == 0) {
            session.setAttribute("result", "Nepodařilo se prodloužit položku, nenachází se v této výpůjčce.");
            return "redirect:/client/" + clientId + "/loans";
        }

        LoanOfItem loanOfItem = loansWithThisItem.get(0);
        extendLoanOfItem(loanOfItem);

        session.setAttribute("result", "Položka " + loanOfItem.getItem().getTitle() + " byla prodloužena o dva týdny.");
        return "redirect:/client/" + clientId + "/loans";
    }


    @Override
    public String returnItemOfLoan(long clientId, long loanId, long itemId, HttpSession session) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se vrátit položku, nejspíše již byla vrácena.");
            return "redirect:/client/" + clientId + "/loans";
        }

        Loan loan = loanOptional.get();
        List<LoanOfItem> loansWithThisItem =
                loan.getItemsOfLoan()
                        .stream()
                        .filter(
                                itemOfLoan -> itemOfLoan.getItem().getIdItem() == itemId)
                        .collect(Collectors.toList());
        if (loansWithThisItem.size() > 1) {
            session.setAttribute("result", "Nepodařilo se vrátit položku, nejspíše již byla vrácena.");
            return "redirect:/client/" + clientId + "/loans";
        } else if (loansWithThisItem.size() == 0) {
            session.setAttribute("result", "Nepodařilo se vrátit položku, nenachází se v této výpůjčce.");
            return "redirect:/client/" + clientId + "/loans";
        }

        LoanOfItem loanOfItem = loansWithThisItem.get(0);
        setStateOfLoanOfItem(loanOfItem);
        setStateToLoanIfAllItemsReturned(loan);

        session.setAttribute("result", "Položka " + loanOfItem.getItem().getTitle() + " byla vrácena.");
        return "redirect:/client/" + clientId + "/loans";
    }

    private void setStateToLoanIfAllItemsReturned(Loan loan) {
        Optional<LoanOfItem> notYetReturnedItemOfLoan =
                loan.getItemsOfLoan()
                        .stream()
                        .filter(
                                itemOfLoan -> itemOfLoan.getState() == LoanState.NOT_YET_RETURNED)
                        .findAny();
        if (notYetReturnedItemOfLoan.isEmpty()) {
            Optional<LoanOfItem> returnedLateItem =
                    loan.getItemsOfLoan()
                            .stream()
                            .filter(
                                    itemOfLoan -> itemOfLoan.getState() == LoanState.RETURNED_LATE)
                            .findAny();
            if (returnedLateItem.isPresent()) {
                loan.setState(LoanState.RETURNED_LATE);
            } else {
                loan.setState(LoanState.RETURNED_ON_TIME);
            }
            loanRepository.save(loan);
        }
    }

    private void setStateOfLoanOfItem(LoanOfItem loanOfItem) {
        LoanState stateToSet;
        if (loanOfItem.getEndDate().isAfter(LocalDate.now())) {
            stateToSet = LoanState.RETURNED_ON_TIME;
        } else {
            stateToSet = LoanState.RETURNED_LATE;
        }

        loanOfItem.setState(stateToSet);
        loanOfItemRepository.save(loanOfItem);
    }


    private void extendLoanOfItem(LoanOfItem loanOfItem) {
        loanOfItem.setEndDate(loanOfItem.getEndDate().plusDays(14));
        loanOfItemRepository.save(loanOfItem);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public void deleteLoan(Loan loan) {
        loanRepository.delete(loan);
    }

    @Override
    public void deleteLoanById(long loanId) {
        loanRepository.deleteById(loanId);
    }

}
