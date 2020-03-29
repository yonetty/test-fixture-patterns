package com.example.testfixturepatterns.objectmother;

import com.example.testfixturepatterns.Book;
import com.example.testfixturepatterns.Loan;
import com.example.testfixturepatterns.Loans;
import com.example.testfixturepatterns.User;

import java.time.LocalDate;

public class UserMother {

    public static User aUserWithoutLoans() {
        Loans loans = Loans.empty();
        return new User("USER", LocalDate.MAX, loans);
    }

    public static User aUserBorrowing2Books() {
        Loans loans = Loans.of(aLoan(), aLoan());
        return new User("USER", LocalDate.MAX, loans);
    }

    public static User aUserBorrowing3Books() {
        Loans loans = Loans.of(aLoan(), aLoan(), aLoan());
        return new User("USER", LocalDate.MAX, loans);
    }

    public static User aUserExpiredAt(LocalDate today) {
        Loans loans = Loans.empty();
        return new User("USER", today.minusDays(1), loans);
    }

    public static User aUserBorrowing2BooksOneOfWhichIsExpiredAt(LocalDate today) {
        Loans loans = Loans.of(aLoan(), anExpiredLoan(today.minusDays(1)));
        return new User("USER", LocalDate.MAX, loans);
    }

    private static Loan aLoan() {
        Book book = new Book("BOOK");
        Loan loan = new Loan(book, LocalDate.MIN, LocalDate.MAX);
        return loan;
    }

    private static Loan anExpiredLoan(LocalDate expirationDate) {
        Book book = new Book("BOOK");
        Loan loan = new Loan(book, LocalDate.MIN, expirationDate);
        return loan;
    }

}
