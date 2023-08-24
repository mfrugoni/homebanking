package com.ap.homebanking.dtos;

import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private int payments;


    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
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

    public long getLoanId() { return loanId; }

    public String getName() { return name; }
}
