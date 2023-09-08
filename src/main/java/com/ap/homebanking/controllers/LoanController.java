package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.services.LoanService;
import com.ap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }

    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication) {

        Client authenticated = clientService.findByEmail(authentication.getName());

        //Params validations:
        if (loanApplicationDTO.getLoanId() <= 0)
            return new ResponseEntity<>("Enter a valid ID", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getAmount() <= 0)
            return new ResponseEntity<>("Amount should be higher than zero", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getPayments() <= 0)
            return new ResponseEntity<>("Payments should be more than zero", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getToAccountNumber().isEmpty())
            return new ResponseEntity<>("Enter an account number", HttpStatus.FORBIDDEN);

        if (!loanService.existsById(loanApplicationDTO.getLoanId()))
            return new ResponseEntity<>("That kind of loan does not exist", HttpStatus.FORBIDDEN);

    //    Option of checking to max amount:
    //    if (loanApplicationDTO.getAmount() > Objects.requireNonNull(loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null)).getMaxAmount())

        if (loanService.existsById(loanApplicationDTO.getLoanId())) {
            if (loanApplicationDTO.getAmount() > loanService.getReferenceById(loanApplicationDTO.getLoanId()).getMaxAmount())
                return new ResponseEntity<>("The amount required is higher than the top amount for this loan", HttpStatus.FORBIDDEN);
        }
        if (!loanService.getReferenceById(loanApplicationDTO.getLoanId()).getPayments().contains(loanApplicationDTO.getPayments()))
            return new ResponseEntity<>("The amount of payments is not available for this kind of loan", HttpStatus.FORBIDDEN);

        if (accountService.findByNumber(loanApplicationDTO.getToAccountNumber()) == null)
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);

        if (!authenticated.getAccounts().contains(accountService.findByNumber(loanApplicationDTO.getToAccountNumber())))
            return new ResponseEntity<>("This account doesn't belong to the authenticated client", HttpStatus.FORBIDDEN);


        //Apply for loan:
        double amountPlusTwenty = loanApplicationDTO.getAmount() * 1.2;
        ClientLoan clientLoan = new ClientLoan(amountPlusTwenty, loanApplicationDTO.getPayments());

        //Bind ClientLoan to client and to loan:
        authenticated.addClientLoan(clientLoan);

        if (loanService.existsById(loanApplicationDTO.getLoanId()))
            loanService.getReferenceById(loanApplicationDTO.getLoanId()).addClientLoan(clientLoan);

        loanService.saveClientLoan(clientLoan);
        clientService.save(authenticated);

        //Create transaction:
        String loanName = loanService.getReferenceById(loanApplicationDTO.getLoanId()).getName();
        Transaction addLoan = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loanName + " - loan approved", LocalDateTime.now());

        Account destinationAccount = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        destinationAccount.addTransaction(addLoan);
        transactionService.save(addLoan);

        //Modify account balance:
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        accountService.save(destinationAccount);

        return new ResponseEntity<>("Loan application approved", HttpStatus.CREATED);

    }

}
