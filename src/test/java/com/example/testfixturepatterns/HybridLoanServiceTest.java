package com.example.testfixturepatterns;

import com.example.testfixturepatterns.hybrid.BookBuilder;
import com.example.testfixturepatterns.hybrid.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.testfixturepatterns.LoanService.LoanCheckResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HybridLoanServiceTest {

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
        User user = UserBuilder.borrowing(2).build();

        Book book = BookBuilder.ofDefault().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(false));
        assertThat(result.errorCode, is(""));
    }

    @Test
    void ユーザーが有効期限切れの場合は貸出不可となる() {
        // Arrange
        User user = UserBuilder.ofDefault().withExpirationDate(YESTERDAY).build();

        Book book = BookBuilder.ofDefault().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("USER_EXPIRED"));
    }

    @Test
    void 既に最大貸出数に達している場合は貸出不可となる() {
        // Arrange
        User user = UserBuilder.borrowing(3).build();

        Book book = BookBuilder.ofDefault().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 最大貸出数を超過する場合は貸出不可となる() {
        // Arrange
        User user = UserBuilder.borrowing(2).build();

        Book[] books = BookBuilder.ofDefault().build(2);

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 貸出中のものに返却期限切れがある場合は新たな貸出は不可となる() {
        // Arrange
        User user = UserBuilder.ofDefault()
                    .borrowing(loanBuilder -> loanBuilder.noop())
                    .borrowing(loanBuilder -> loanBuilder.withDueDate(YESTERDAY))
                    .build();

        Book book = BookBuilder.ofDefault().build();

        // Act
        LoanCheckResult result = sut.check(user, book);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("UNRETURNED_BOOKS"));
    }

    @Test
    void 禁帯出の本が含まれている場合は貸出不可となる() {
        // Arrange
        User user = UserBuilder.ofDefault().build();

        Book[] books = new Book[]{BookBuilder.ofDefault().build(),
                BookBuilder.ofDefault().asInLibraryUse().build()};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("IN_LIBRARY_USE"));
    }

}
