package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private long Id;
    private String firstName;
    private String lastName;
    private String email;

    //Set<Account> accounts = new HashSet<>();

    public ClientDTO(Client client){
        this.Id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();
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
}
