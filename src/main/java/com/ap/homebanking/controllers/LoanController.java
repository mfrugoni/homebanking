package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication){

        Client authenticated = clientRepository.findByEmail(authentication.getName());

        if (loanApplicationDTO.getLoanId() <= 0)
            return new ResponseEntity<>("Enter a valid ID", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getAmount() <= 0)
            return new ResponseEntity<>("Amount should be higher than zero", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getPayments() <= 0)
            return new ResponseEntity<>("Payments should be more than zero", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getToAccountNumber().isEmpty())
            return new ResponseEntity<>("Enter an account number", HttpStatus.FORBIDDEN);

        if (!loanRepository.existsById(loanApplicationDTO.getLoanId()))
            return new ResponseEntity<>("That kind of loan does not exist", HttpStatus.FORBIDDEN);

        if (loanRepository.existsById(loanApplicationDTO.getLoanId())){
            if (loanApplicationDTO.getAmount() > loanRepository.getReferenceById(loanApplicationDTO.getLoanId()).getMaxAmount())
                return new ResponseEntity<>("The amount required is higher than the top amount for this loan", HttpStatus.FORBIDDEN);
        }
        if (!loanRepository.getReferenceById(loanApplicationDTO.getLoanId()).getPayments().contains(loanApplicationDTO.getPayments()))
            return new ResponseEntity<>("The amount of payments is not available for this kind of loan", HttpStatus.FORBIDDEN);

        if (accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber()) == null)
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);

        if (!authenticated.getAccounts().contains(accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber())))
            return new ResponseEntity<>("This account doesn't belong to the authenticated client", HttpStatus.FORBIDDEN);

       // if (loanApplicationDTO.getAmount() > loanRepository.findById(loanApplicationDTO.getLoanId()))





        return new ResponseEntity<>("", HttpStatus.CREATED);

    }

}
