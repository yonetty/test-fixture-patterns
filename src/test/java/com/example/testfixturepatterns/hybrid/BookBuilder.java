package com.example.testfixturepatterns.hybrid;

import com.example.testfixturepatterns.Book;

import java.util.ArrayList;
import java.util.List;

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

    public static BookBuilder ofDefault() {
        return new BookBuilder();
    }

    public Book build() {
        return new Book(title, inLibraryUse);
    }

    public Book[] build(int numOfBooks) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < numOfBooks; i++) {
            Book book = build();
            books.add(book);
        }
        return books.toArray(new Book[0]);

    }
}
