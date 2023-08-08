package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long Id;
    private String firstName;
    private String lastName;
    private String email;

    Set<AccountDto> accounts;

    public ClientDTO(Client client){
        this.Id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccounts().stream().map(account -> new AccountDto(account)).collect(toSet());

               //
    }

    public long getId() {
        return Id;
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

    public Set<AccountDto> getAccounts() {
        return accounts;
    }
}
