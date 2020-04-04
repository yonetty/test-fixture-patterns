package com.example.testfixturepatterns.builder

import com.example.testfixturepatterns.Book
import com.example.testfixturepatterns.Loan
import com.example.testfixturepatterns.Loans
import com.example.testfixturepatterns.User

import java.time.LocalDate

class GroovyUserBuilder extends FactoryBuilderSupport {

    GroovyUserBuilder() {
        registerFactory('user', new UserFactory())
        registerFactory('borrowing', new LoanFactory())
        registerFactory('book', new BookFactory())
    }
}

class UserFactory extends AbstractFactory {

    static final DEFAULTS = [name: 'DUMMY', expirationDate: LocalDate.MAX, loans: Loans.empty()]

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        def props = DEFAULTS + attributes
        props as User
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.loans.add(child)
    }
}

class LoanFactory extends AbstractFactory {

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        attributes as Loan
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.book = child
    }
}

class BookFactory extends AbstractFactory {

    boolean isLeaf() {
        true
    }

    def newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        attributes as Book
    }
}


