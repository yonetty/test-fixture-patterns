package com.example.testfixturepatterns;

import java.time.LocalDate;
import java.util.stream.Stream;

public class LoanService {

    public static final int LOAN_LIMIT = 3;

    private LocalDate today;

    public LoanService(LocalDate today) {
        this.today = today;
    }

    public LoanCheckResult check(User user, Book[] books) {
        if (user.isExpiredAt(today)) {
            return LoanCheckResult.error("USER_EXPIRED");
        }

        Loans loans = user.getLoans();
        if (loans.count() + books.length > LOAN_LIMIT) {
            return LoanCheckResult.error("LIMIT_EXCEEDED");
        }
        if (loans.hasExpiredItemAt(today)) {
            return LoanCheckResult.error("UNRETURNED_BOOKS");
        }
        boolean existsInLibraryUse =
                Stream.of(books)
                    .anyMatch(it -> it.isInLibraryUse());
        if (existsInLibraryUse) {
            return LoanCheckResult.error("IN_LIBRARY_USE");
        }
        return LoanCheckResult.ok();
    }

    static class LoanCheckResult {

        public final boolean hasError;
        public final String errorCode;

        private LoanCheckResult(boolean hasError, String errorCode) {
            this.hasError = hasError;
            this.errorCode = errorCode;
        }

        private static LoanCheckResult ok() {
            return new LoanCheckResult(false, "");
        }

        private static LoanCheckResult error(String errorCode) {
            return new LoanCheckResult(true, errorCode);
        }
    }
}
