package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Client;
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





        return new ResponseEntity<>("", HttpStatus.CREATED);

    }

}
