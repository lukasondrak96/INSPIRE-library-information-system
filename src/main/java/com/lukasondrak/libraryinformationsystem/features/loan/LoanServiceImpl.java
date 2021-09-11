package com.lukasondrak.libraryinformationsystem.features.loan;

import com.lukasondrak.libraryinformationsystem.features.author.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    @Override
    public Optional<Loan> findById(long loanId) {
        return loanRepository.findById(loanId);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
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
