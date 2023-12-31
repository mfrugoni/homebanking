package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @Transactional
    @PostMapping("/transactions")
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
        Account accountFrom = accountService.findByNumber(fromAccountNumber);
        Account accountTo = accountService.findByNumber(toAccountNumber);

        if (accountFrom == null)
            return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);

        if (accountTo == null)
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);

        //Validate if auth client owns account:
        Client authenticated = clientService.findByEmail(authentication.getName());

        if (!authenticated.getAccounts().contains(accountFrom))
            return new ResponseEntity<>("Origin account doesn't belong to the auth client", HttpStatus.FORBIDDEN);

        //Validate if there's enough money to make the transaction:
        if (amount > accountFrom.getBalance())
            return new ResponseEntity<>("Balance is less than the amount to transfer", HttpStatus.FORBIDDEN);

        //Make transaction:
        double debitAmount = amount * -1;
        String transactionDescriptionFrom = description + " " + toAccountNumber;
        String transactionDescriptionTo = description + " " + fromAccountNumber;

        Transaction sendMoney = new Transaction(TransactionType.DEBIT, debitAmount, transactionDescriptionFrom, LocalDateTime.now());
        Transaction getMoney = new Transaction(TransactionType.CREDIT, amount, transactionDescriptionTo, LocalDateTime.now());

        accountFrom.addTransaction(sendMoney);
        accountTo.addTransaction(getMoney);
        transactionService.save(sendMoney);
        transactionService.save(getMoney);

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountService.save(accountFrom);
        accountService.save(accountTo);

        return new ResponseEntity<>("Transaction completed", HttpStatus.CREATED);
    }
}
