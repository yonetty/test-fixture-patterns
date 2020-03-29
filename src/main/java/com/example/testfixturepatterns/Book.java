package com.example.testfixturepatterns;

public class Book {

    private String title;
    private boolean inLibraryUse;

    public Book(String title) {
        this(title, false);
    }

    public Book(String title, boolean inLibraryUse) {
        this.title = title;
        this.inLibraryUse = inLibraryUse;
    }

    public boolean isInLibraryUse() {
        return inLibraryUse;
    }
}
