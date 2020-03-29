package com.example.testfixturepatterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.testfixturepatterns.LoanService.LoanCheckResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleLoanServiceTest {

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    private static final LocalDate A_WEEK_AGO = TODAY.minusDays(7);

    private LoanService sut;

    @BeforeEach
    void setup() {
        sut = new LoanService(TODAY);
    }

    @Test
    void 何もエラーがなければ貸出可能となる() {
        // Arrange
        Book book1 = new Book("枕草子");
        Loan loan1 = new Loan(book1, A_WEEK_AGO, TODAY);
        Book book2 = new Book("源氏物語");
        Loan loan2 = new Loan(book2, A_WEEK_AGO, TODAY);

        Loans loans = Loans.of(loan1, loan2);

        User user = new User("山本", TODAY, loans);

        Book[] books = new Book[]{new Book("竹取物語")};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(false));
        assertThat(result.errorCode, is(""));
    }

    @Test
    void ユーザーが有効期限切れの場合は貸出不可となる() {
        // Arrange
        Book book1 = new Book("枕草子");
        Loan loan1 = new Loan(book1, A_WEEK_AGO, TODAY);
        Book book2 = new Book("源氏物語");
        Loan loan2 = new Loan(book2, A_WEEK_AGO, TODAY);

        Loans loans = Loans.of(loan1, loan2);

        User user = new User("山本", YESTERDAY, loans);

        Book[] books = new Book[]{new Book("竹取物語")};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("USER_EXPIRED"));
    }

    @Test
    void 既に最大貸出数に達している場合は貸出不可となる() {
        // Arrange
        Book book1 = new Book("枕草子");
        Loan loan1 = new Loan(book1, A_WEEK_AGO, TODAY);
        Book book2 = new Book("源氏物語");
        Loan loan2 = new Loan(book2, A_WEEK_AGO, TODAY);
        Book book3 = new Book("平家物語");
        Loan loan3 = new Loan(book3, A_WEEK_AGO, TODAY);

        Loans loans = Loans.of(loan1, loan2, loan3);

        User user = new User("山本", TODAY, loans);

        Book[] books = new Book[]{new Book("竹取物語")};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 最大貸出数を超過する場合は貸出不可となる() {
        // Arrange
        Book book1 = new Book("枕草子");
        Loan loan1 = new Loan(book1, A_WEEK_AGO, TODAY);
        Book book2 = new Book("源氏物語");
        Loan loan2 = new Loan(book2, A_WEEK_AGO, TODAY);

        Loans loans = Loans.of(loan1, loan2);

        User user = new User("山本", TODAY, loans);

        Book[] books = new Book[]{new Book("竹取物語"), new Book("平家物語")};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("LIMIT_EXCEEDED"));
    }

    @Test
    void 貸出中のものに返却期限切れがある場合は新たな貸出は不可となる() {
        // Arrange
        Book book1 = new Book("枕草子");
        Loan loan1 = new Loan(book1, A_WEEK_AGO, TODAY);
        Book book2 = new Book("源氏物語");
        Loan loan2 = new Loan(book2, A_WEEK_AGO, YESTERDAY);

        Loans loans = Loans.of(loan1, loan2);

        User user = new User("山本", TODAY, loans);

        Book[] books = new Book[]{new Book("竹取物語")};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("UNRETURNED_BOOKS"));

    }

    @Test
    void 禁帯出の本が含まれている場合は貸出不可となる() {
        // Arrange
        Loans loans = Loans.empty();

        User user = new User("山本", TODAY, loans);

        Book inLibraryUseBook = new Book("大辞林", true);
        Book[] books = new Book[]{new Book("竹取物語"), inLibraryUseBook};

        // Act
        LoanCheckResult result = sut.check(user, books);

        // Assert
        assertThat(result.hasError, is(true));
        assertThat(result.errorCode, is("IN_LIBRARY_USE"));

    }

}
