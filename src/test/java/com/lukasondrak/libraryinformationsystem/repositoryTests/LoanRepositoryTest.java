package com.lukasondrak.libraryinformationsystem.repositoryTests;

import com.lukasondrak.libraryinformationsystem.features.loan.Loan;
import com.lukasondrak.libraryinformationsystem.features.loan.LoanRepository;
import com.lukasondrak.libraryinformationsystem.features.loan.LoanState;
import com.lukasondrak.libraryinformationsystem.features.loanofitem.LoanOfItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableTransactionManagement
public class LoanRepositoryTest {

    public static final long ID_NOT_IN_DB = 123456L;
    public static final LoanState MOCK_STATE = LoanState.NOT_YET_RETURNED;
    public static final LocalDate MOCK_START_DATE = LocalDate.of(12, 12, 12);
    public static final LocalDate MOCK_END_DATE = LocalDate.of(12, 12, 24);

    private static Loan loan;

    @Autowired
    private LoanRepository loanRepository;


    @BeforeEach
    public void beforeEach() {
        loanRepository.deleteAll();
        loan = new Loan(MOCK_STATE, MOCK_START_DATE);
    }

    @AfterEach
    public void afterEach() {
        loanRepository.deleteAll();
    }

    @Test
    public void saveLoanWithLoanItemsAndFindByIdTest() {
        LoanOfItem loanOfItem = new LoanOfItem(MOCK_STATE, MOCK_END_DATE);
        loan.getItemsOfLoan().add(loanOfItem);
        Loan savedLoan = loanRepository.save(loan);

        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getIdLoan());

        assertTrue(foundLoan.isPresent(), "We should find some loan");
        assertEquals(savedLoan, foundLoan.get(), "We should get the same loan as the loan we saved");
        assertEquals(MOCK_STATE, foundLoan.get().getState(), "We should get the same loan state as the loans state we saved");
        assertEquals(MOCK_START_DATE, foundLoan.get().getStartDate(), "We should get the same loan start date as the loans start date we saved");

    }

    @Test
    public void saveLoanWithoutLoanItemsTest() {
        assertThrows(TransactionSystemException.class, () -> {
            loanRepository.save(loan);
        });


        List<Loan> allLoans = loanRepository.findAll();

        assertTrue(allLoans.isEmpty(), "There should not be some loan");
    }

    @Test
    public void findAllTest() {
        Loan loan2 = new Loan(LoanState.RETURNED_ON_TIME, MOCK_START_DATE);
        LoanOfItem loanOfItem = new LoanOfItem(MOCK_STATE, MOCK_END_DATE);
        LoanOfItem loanOfItem2 = new LoanOfItem(MOCK_STATE, MOCK_END_DATE);
        loan.getItemsOfLoan().add(loanOfItem);
        loan2.getItemsOfLoan().add(loanOfItem2);

        List<Loan> savedLoanList = new ArrayList<>();
        savedLoanList.add(loanRepository.save(loan));
        savedLoanList.add(loanRepository.save(loan2));

        List<Loan> allLoans = loanRepository.findAll();

        assertFalse(allLoans.isEmpty(), "There should be some loan");
        assertEquals(2, allLoans.size(), "There should be only two loans");
        assertEquals(allLoans, savedLoanList, "Saved loans should be the same as the loans we saved");
    }

    @Test
    public void findByNotExistentIdTest() {
        Optional<Loan> foundLoan = loanRepository.findById(ID_NOT_IN_DB);

        assertTrue(foundLoan.isEmpty(), "There should not be any loan with this ID");
    }

    @Test
    public void deleteTest() {
        LoanOfItem loanOfItem = new LoanOfItem(MOCK_STATE, MOCK_END_DATE);
        loan.getItemsOfLoan().add(loanOfItem);
        Loan savedLoan = loanRepository.save(loan);

        loanRepository.delete(savedLoan);
        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getIdLoan());

        assertTrue(foundLoan.isEmpty(), "There should not be any loan with this ID");
    }

    @Test
    public void deleteNotExistentLoanTest() {
        loanRepository.delete(loan);

        List<Loan> allLoans = loanRepository.findAll();

        assertTrue(allLoans.isEmpty(), "There should not be any loan");
    }

}
