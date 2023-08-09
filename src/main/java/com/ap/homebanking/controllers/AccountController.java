package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @RequestMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id){
        return accountRepository.findById(id).orElse(null);
    }

}
