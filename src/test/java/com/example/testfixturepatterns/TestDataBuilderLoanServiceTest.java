package com.example.testfixturepatterns;

import com.example.testfixturepatterns.builder.BookBuilder;
import com.example.testfixturepatterns.builder.LoanBuilder;
import com.example.testfixturepatterns.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.testfixturepatterns.LoanService.LoanCheckResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestDataBuilderLoanServiceTest {

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);

    private LoanService sut;

    @BeforeEach
    void setup() {
        sut = new LoanService(TODAY);
    }

    @Test
    void 何もエラーがなければ貸出可能となる() {
        // Arrange
        Loan loan1 = new LoanBuilder().build();
        Loan loan2 = new LoanBuilder().build();

        Loans loans = Loans.of(loan1, loan2);

        User user = new UserBuilder().withLoans(loans).build();

        Book book = new BookBuilder().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(false));
        assertThat(result.errorCode, is(""));
    }

    @Test
    void ユーザーが有効期限切れの場合は貸出不可となる() {
        // Arrange
        User user = new UserBuilder().withExpirationDate(YESTERDAY).build();

        Book book = new BookBuilder().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("USER_EXPIRED"));
    }

    @Test
    void 既に最大貸出数に達している場合は貸出不可となる() {
        // Arrange
        Loan loan1 = new LoanBuilder().build();
        Loan loan2 = new LoanBuilder().build();
        Loan loan3 = new LoanBuilder().build();

        Loans loans = Loans.of(loan1, loan2, loan3);

        User user = new UserBuilder().withLoans(loans).build();

        Book book = new BookBuilder().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 最大貸出数を超過する場合は貸出不可となる() {
        // Arrange
        Loan loan1 = new LoanBuilder().build();
        Loan loan2 = new LoanBuilder().build();

        Loans loans = Loans.of(loan1, loan2);

        User user = new UserBuilder().withLoans(loans).build();

        Book[] books = new Book[]{new BookBuilder().build(), new BookBuilder().build()};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 貸出中のものに返却期限切れがある場合は新たな貸出は不可となる() {
        // Arrange
        Loan loan1 = new LoanBuilder().build();
        Loan loan2 = new LoanBuilder().withDueDate(YESTERDAY).build();

        Loans loans = Loans.of(loan1, loan2);

        User user = new UserBuilder().withLoans(loans).build();

        Book book = new BookBuilder().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("UNRETURNED_BOOKS"));

    }

    @Test
    void 禁帯出の本が含まれている場合は貸出不可となる() {
        // Arrange
        Loans loans = Loans.empty();

        User user = new UserBuilder().withLoans(loans).build();

        Book[] books = new Book[]{new BookBuilder().build(),
                new BookBuilder().asInLibraryUse().build()};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("IN_LIBRARY_USE"));

    }

}
