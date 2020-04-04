package com.example.testfixturepatterns;

import java.time.LocalDate;

public class Loan {

    private Book book;

    private LocalDate loanDate;

    private LocalDate dueDate;

    public Loan(Book book, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    private Loan() {}

    public boolean isExpiredAt(LocalDate date) {
        return dueDate.isBefore(date);
    }
}
