package com.example.testfixturepatterns.objectmother;

import com.example.testfixturepatterns.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMother {

    public static Book aBook() {
        return new Book("BOOK");
    }

    public static Book aBookInLibraryUse() {
        return new Book("BOOK", true);
    }

    public static Book[] books(int num) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < num ; i++) {
            books.add(aBook()) ;
        }
        return books.toArray(new Book[0]);
    }

    public static Book[] booksContainingAnBookInLibraryUse() {
        return new Book[] { aBook(), aBookInLibraryUse()} ;
    }
}
