package com.ap.homebanking.dtos;

public class LoanApplicationDTO {
private int loanId;
private double amount;
private int payments;
private String toAccountNumber;

    public int getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
