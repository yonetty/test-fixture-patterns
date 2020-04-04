package com.example.testfixturepatterns

import com.example.testfixturepatterns.builder.GroovyUserBuilder
import spock.lang.Specification

import java.time.LocalDate

class GroovyBuilderLoanServiceSpec extends Specification {

    static final LocalDate TODAY = LocalDate.now()
    static final LocalDate YESTERDAY = TODAY.minusDays(1L)
    static final LocalDate A_WEEK_AGO = TODAY.minusDays(7L)

    LoanService sut

    def setup() {
        sut = new LoanService(TODAY)
    }

    def '貸出中のものに返却期限切れがある場合は新たな貸出は不可となる'() {
        given: "返却期限切れの貸出があるユーザー"
        def builder = new GroovyUserBuilder()
        def user = builder.user(name: '山本') {
            borrowing(loanDate: A_WEEK_AGO, dueDate: TODAY) {
                book(title: "枕草子")
            }
            borrowing(loanDate: A_WEEK_AGO, dueDate: YESTERDAY) {
                book(title: "源氏物語")
            }
        }

        and: "新規に借りたい本"
        def book = new Book('竹取物語')

        when: "チェックを実行"
        def result = sut.check(user, book)

        then: "チェックエラー"
        result.hasError
        result.errorCode == 'UNRETURNED_BOOKS'
    }
}
