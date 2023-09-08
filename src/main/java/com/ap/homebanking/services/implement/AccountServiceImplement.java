package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
