package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            Authentication authentication){

        //Params validations:
        if (amount <= 0)
            return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);

        if (description.isEmpty())
            return new ResponseEntity<>("Enter a description", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty())
            return new ResponseEntity<>("Account fields can't be empty", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.equals(toAccountNumber))
            return new ResponseEntity<>("Accounts' numbers can't be the same", HttpStatus.FORBIDDEN);

        //Validate if accounts exist:
        Account accountFrom = accountRepository.findByNumber(fromAccountNumber);
        Account accountTo = accountRepository.findByNumber(toAccountNumber);

        if (accountFrom == null)
            return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);

        if (accountTo == null)
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);

        //Validate if auth client owns account:
        Client authenticated = clientRepository.findByEmail(authentication.getName());

        if (!authenticated.getAccounts().contains(accountFrom))
            return new ResponseEntity<>("Origin account doesn't belong to the auth client", HttpStatus.FORBIDDEN);


//        Set<Account> fromAccounts = new HashSet<>();
//        fromAccounts = authenticated.getAccounts();
//        Object[] belongs = fromAccounts.stream().filter(account -> account.getNumber().equals(fromAccountNumber)).toArray();
//        if (belongs.length == 0)
//            return new ResponseEntity<>("This account doesn't belong to the authenticated client", HttpStatus.FORBIDDEN);



        return new ResponseEntity<>("", HttpStatus.CREATED);

    }
}
