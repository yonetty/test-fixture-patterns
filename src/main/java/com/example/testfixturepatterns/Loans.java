package com.example.testfixturepatterns;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loans {

    private List<Loan> loanList;

    public static Loans of(Loan... loans) {
        List<Loan> loanList = Arrays.asList(loans);
        return new Loans(loanList);
    }

    public static Loans empty() {
        return new Loans(new ArrayList<>());
    }

    private Loans(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public int count() {
        return loanList.size();
    }

    public boolean hasExpiredItemAt(LocalDate date) {
        return loanList.stream().anyMatch(it -> it.isExpiredAt(date));
    }
}
