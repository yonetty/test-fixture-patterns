package com.example.testfixturepatterns;

import java.time.LocalDate;

public class UserBuilder {

    private String name = "DEFAULT_NAME";
    private LocalDate expirationDate = LocalDate.MAX;
    private Loans loans = Loans.empty();

    public UserBuilder withLoans(Loans loans) {
        this.loans = loans;
        return this;
    }

    public UserBuilder withExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public User build() {
        return new User(name, expirationDate, loans);
    }
}
