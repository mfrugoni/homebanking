package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        Client authenticated = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);

        if (account != null){
            if (authenticated.getAccounts().contains(account)){
                return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("This account doesn't belong to the authenticated client", HttpStatus.FORBIDDEN);
            }
        }
        else {
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client authenticated = clientRepository.findByEmail(authentication.getName());

        Set<Account> totalAccounts = new HashSet<>();
        totalAccounts = authenticated.getAccounts();

        if (totalAccounts.size() >= 3)
            return new ResponseEntity<>("You can´t own more than three accounts", HttpStatus.FORBIDDEN);

        else {
            Integer number = getRandomNumber(0, 99999999);
            String accountNumber = "VIN-" + number;

            Account account = new Account(accountNumber, LocalDate.now(), 0 );
            authenticated.addAccount(account);
            accountRepository.save(account);
            return new ResponseEntity<>("New account created", HttpStatus.CREATED);
        }
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client authenticated = clientRepository.findByEmail(authentication.getName());
        return authenticated.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

}
