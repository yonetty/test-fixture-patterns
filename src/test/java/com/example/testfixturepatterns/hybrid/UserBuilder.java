package com.example.testfixturepatterns.hybrid;

import com.example.testfixturepatterns.Loan;
import com.example.testfixturepatterns.Loans;
import com.example.testfixturepatterns.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    public static UserBuilder ofDefault() {
        return new UserBuilder();
    }

    public static UserBuilder borrowing(int numOfBooks) {
        UserBuilder builder = new UserBuilder();
        List<Loan> loanList = new ArrayList<>();
        for (int i = 0; i < numOfBooks; i++) {
            Loan loan = new LoanBuilder().build();
            loanList.add(loan);
        }
        builder.loans = Loans.of(loanList.toArray(new Loan[0]));
        return builder;
    }

    public UserBuilder borrowing(Consumer<LoanBuilder> callback) {
        LoanBuilder loanBuilder = LoanBuilder.ofDefault();
        callback.accept(loanBuilder);
        loans.add(loanBuilder.build());
        return this;
    }

    public User build() {
        return new User(name, expirationDate, loans);
    }
}
