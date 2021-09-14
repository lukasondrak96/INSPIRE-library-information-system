package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.client.Client;
import com.lukasondrak.libraryinformationsystem.features.client.ClientService;
import com.lukasondrak.libraryinformationsystem.features.item.Item;
import com.lukasondrak.libraryinformationsystem.features.item.ItemService;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItemRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lukasondrak.libraryinformationsystem.common.HttpSessionManipulator.clearSessionResultAttribute;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private LoanRepository loanRepository;
    private LoanOfItemRepository loanOfItemRepository;
    private ItemService itemService;
    private ClientService clientService;


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
    public String returnAllItemsOfLoan(long clientId, long loanId, HttpSession session) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se vrátit položky vypůjčky, nejspíše již byla celá vrácena.");
            return "redirect:/client/" + clientId + "/loans";
        }


        Loan loan = loanOptional.get();
        List<LoanOfItem> itemsOfLoan = new ArrayList<>(loan.getItemsOfLoan());
        itemsOfLoan.stream().forEach(this::returnLoanOfItem);

        returnLoanIfAllItemsReturned(loan);

        session.setAttribute("result",
                "Položky vypůjčky ze dne " +
                        loan.dateToEuropeanTimeFormat(loan.getStartDate()) +
                        " byly vráceny."
        );
        return "redirect:/client/" + clientId + "/loans";
    }

    @Override
    public String prepareNewClientsLoanPage(long clientId, Model model, HttpSession session) {
        Optional<Client> clientOptional = clientService.findById(clientId);
        if (clientOptional.isEmpty()) {
            session.setAttribute("result", "Uživatel s id " + clientId + " nebyl nalezen.");
            return "redirect:/clients";
        }
        List<Item> allItemsInDb = itemService.getAllItems();
        if (allItemsInDb.isEmpty()) {
            session.setAttribute("result", "Nelze vytvořit výpůjčka. V databázi se nenachází žádná položka.");
            return "redirect:/clients";
        }

        model.addAttribute("client", clientOptional.get());
        model.addAttribute("items", allItemsInDb);

        model.addAttribute("result", session.getAttribute("result"));
        Loan loan = new Loan();
        loan.setState(LoanState.NOT_YET_RETURNED);
        loan.setStartDate(LocalDate.now());
        model.addAttribute("loan", loan);

        clearSessionResultAttribute(session);
        return "pages/loan/newClientsLoan";
    }

    @Override
    public String addNewLoanToClient(Loan newLoan, BindingResult result, long clientId, HttpSession session) {



        session.setAttribute("result", "Výpůjčka byla úspěšně přidána.");
        return "redirect:/client/" + clientId + "/loans";
    }

    @Override
    public String deleteItemOfLoanOfClient(long clientId, long loanId, long itemId, HttpSession session) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            LOGGER.error("Error while deleting item, item is not in database");
            session.setAttribute("result", "Nepodařilo se odstanit položku, nejspíše již byla odstaněna.");
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
            LOGGER.error("Error while deleting item, item is not in database");
            session.setAttribute("result", "Nepodařilo se odstanit položku, nejspíše již byla odstaněna.");
            return "redirect:/client/" + clientId + "/loans";
        } else if (loansWithThisItem.size() == 0) {
            LOGGER.error("Error while deleting item, item is not in database");
            session.setAttribute("result", "Nepodařilo se odstanit položku, nenachází se v této výpůjčce.");
            return "redirect:/client/" + clientId + "/loans";
        }


        LoanOfItem loanOfItem = loansWithThisItem.get(0);
        loan.getItemsOfLoan().remove(loanOfItem);
        loanRepository.save(loan);
        LOGGER.debug("Item " + loanOfItem.getItem().getTitle() + " successfully deleted of loan with id " + loanOfItem.getLoan().getIdLoan());

        session.setAttribute("result", "Položka " + loanOfItem.getItem().getTitle() + " byla odstraněna.");

        returnLoanIfAllItemsDeleted(loan, loanOfItem, session);

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
        returnLoanOfItem(loanOfItem);
        returnLoanIfAllItemsReturned(loan);

        session.setAttribute("result", "Položka " + loanOfItem.getItem().getTitle() + " byla vrácena.");
        return "redirect:/client/" + clientId + "/loans";
    }

    @Override
    public String deleteLoanOfClient(long loanId, long clientId, HttpSession session) {
        List<Loan> allLoans = loanRepository.findAll();
        session.setAttribute("loans", allLoans);

        Optional<Loan> loanToDeleteOptional = loanRepository.findById(loanId);
        if (loanToDeleteOptional.isEmpty()) {
            session.setAttribute("result", "Nepodařilo se smazat výpůjčku. Výpůjčka se nenachází v databázi.");
        } else {
            loanRepository.deleteById(loanId);
            Loan deletedLoan = loanToDeleteOptional.get();
            session.setAttribute("result",
                    "Výpůjčka ze dne " +
                            deletedLoan.dateToEuropeanTimeFormat(deletedLoan.getStartDate()) +
                            " byla úspěšně vymazána.");
        }
        return "redirect:/client/" + clientId + "/loans";
    }

    private void returnLoanIfAllItemsReturned(Loan loan) {
        Optional<LoanOfItem> notYetReturnedItemOfLoan = getLoanOfItemWithState(loan, LoanState.NOT_YET_RETURNED);
        if (notYetReturnedItemOfLoan.isEmpty()) {
            Optional<LoanOfItem> returnedLateItem = getLoanOfItemWithState(loan, LoanState.RETURNED_LATE);
            if (returnedLateItem.isPresent()) {
                loan.setState(LoanState.RETURNED_LATE);
            } else {
                loan.setState(LoanState.RETURNED_ON_TIME);
            }
            loanRepository.save(loan);
        }
    }

    private Optional<LoanOfItem> getLoanOfItemWithState(Loan loan, LoanState state) {
        return loan.getItemsOfLoan()
                .stream()
                .filter(
                        itemOfLoan -> itemOfLoan.getState() == state)
                .findAny();
    }

    private void returnLoanOfItem(LoanOfItem loanOfItem) {
        boolean alreadyReturned =
                loanOfItem.getState() == LoanState.RETURNED_ON_TIME ||
                        loanOfItem.getState() == LoanState.RETURNED_LATE;
        if (alreadyReturned)
            return;

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

    private void returnLoanIfAllItemsDeleted(Loan loan, LoanOfItem loanOfItem, HttpSession session) {
        if (loan.getItemsOfLoan().isEmpty()) {
            loanRepository.delete(loan);
            session.setAttribute("result",
                    "Položka " +
                            loanOfItem.getItem().getTitle() + " byla odstraněna. " +
                            "Zároveň byla odstraněna celá výpůjčka ze dne " +
                            loan.dateToEuropeanTimeFormat(loan.getStartDate()) +
                            ", protože již neobsahovala žádné položky.");

        }
    }

}
