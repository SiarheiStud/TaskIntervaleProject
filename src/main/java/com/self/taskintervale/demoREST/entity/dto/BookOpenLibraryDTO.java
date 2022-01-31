package com.self.taskintervale.demoREST.entity.dto;

import java.util.Objects;

public class BookOpenLibraryDTO {

    private String ISBN;
    private String title;
    private String author;
    private int numberOfPages;

    public BookOpenLibraryDTO() {
    }

    public BookOpenLibraryDTO(String ISBN, String title, String author, int numberOfPages) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookOpenLibraryDTO that = (BookOpenLibraryDTO) o;
        return numberOfPages == that.numberOfPages && Objects.equals(ISBN, that.ISBN) && Objects.equals(title, that.title) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN, title, author, numberOfPages);
    }
}
