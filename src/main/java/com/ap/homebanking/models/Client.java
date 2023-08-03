package com.ap.homebanking.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {
    @Id
    private long Id;
    private String firstName;
    private String lastName;
    private String email;

    public Client(){}

    public Client(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
