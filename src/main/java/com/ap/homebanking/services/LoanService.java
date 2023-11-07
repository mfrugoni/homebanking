package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoans();
    void saveLoan(Loan loan);
    void saveClientLoan(ClientLoan clientLoan);
    boolean existsById(Long id);
    Loan getReferenceById(Long id);
}
