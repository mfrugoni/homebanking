package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;
    private Client client;
    private Loan loan;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();

        this.amount = clientLoan.getAmount();

        this.payments = clientLoan.getPayments();

        this.client = clientLoan.getClient();

        this.loan = clientLoan.getLoan();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }
}
