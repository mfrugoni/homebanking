package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> accounts;

    private Set<ClientLoanDTO> loans;

    private Set<CardDTO> cards;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());
        this.loans = client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() { return loans; }

    public Set<CardDTO> getCards() { return cards; }
}
