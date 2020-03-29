package com.example.testfixturepatterns;

import java.time.LocalDate;

public class User {

    private String name;
    private Loans loans;
    private LocalDate expirationDate;

    public User(String name, LocalDate expirationDate, Loans loans) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.loans = loans;
    }

    public Loans getLoans() {
        return loans;
    }

    public boolean isExpiredAt(LocalDate date) {
        return expirationDate.isBefore(date);
    }
}
