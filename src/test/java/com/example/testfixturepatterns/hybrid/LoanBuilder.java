package com.example.testfixturepatterns.hybrid;

import com.example.testfixturepatterns.Book;
import com.example.testfixturepatterns.Loan;

import java.time.LocalDate;

public class LoanBuilder {

    private Book book = new BookBuilder().build();
    private LocalDate loanDate = LocalDate.MIN;
    private LocalDate dueDate = LocalDate.MAX;


    public static LoanBuilder ofDefault() {
        return new LoanBuilder();
    }

    public LoanBuilder withBook(Book book) {
        this.book = book;
        return this;
    }

    public LoanBuilder withLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
        return this;
    }

    public LoanBuilder withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void noop() {
    }

    public Loan build() {
        return new Loan(book, loanDate, dueDate);
    }
}
