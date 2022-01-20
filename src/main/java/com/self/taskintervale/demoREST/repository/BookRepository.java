package com.self.taskintervale.demoREST.repository;

import com.self.taskintervale.demoREST.entity.BookEntity;

import java.util.List;

public interface BookRepository {

    List<BookEntity> getBooks();
    void saveBook(BookEntity bookEntity);
    void updateBook(BookEntity bookEntity);
    void deleteBook(Long id);

    boolean isContainsId(Long id);
    boolean isContainsISBN(BookEntity bookEntity);
    boolean otherBookContainsISBN(BookEntity bookEntity);

}
