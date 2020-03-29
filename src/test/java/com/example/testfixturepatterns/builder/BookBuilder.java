package com.example.testfixturepatterns.builder;

import com.example.testfixturepatterns.Book;

public class BookBuilder {

    private String title = "BOOK";
    private boolean inLibraryUse = false;

    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder asInLibraryUse() {
        this.inLibraryUse = true;
        return this;
    }

    public Book build() {
        return new Book(title, inLibraryUse);
    }
}
