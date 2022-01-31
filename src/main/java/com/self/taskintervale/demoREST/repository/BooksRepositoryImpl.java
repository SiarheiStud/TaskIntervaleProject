package com.self.taskintervale.demoREST.repository;

import com.self.taskintervale.demoREST.entity.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BooksRepositoryImpl implements BookRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BooksRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // CRUD запросы
    private static final String GET_BOOKS = "SELECT * FROM book";
    private static final String ADD_BOOK = "INSERT INTO book (ISBN, title, author, numberOfPages, weight, price)" +
            "VALUES(?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_BOOK = "Update book SET ISBN=?, title=?, author=?, numberOfPages=?, weight=?, " +
            "price=? WHERE id=?";
    private static final String DELETE_BOOK = "DELETE FROM book WHERE id=?";

    // Вспомогательные запросы
    private static final String FIND_BOOK_BY_ID = "SELECT * FROM book WHERE id=?";
    private static final String FIND_BOOK_BY_ISBN = "SELECT * FROM book WHERE ISBN=?";
    private static final String FIND_OTHER_BOOK_WITH_ISBN = "SELECT * FROM book WHERE ISBN=? AND id<>?";
    private static final String FIND_BOOK_BY_AUTHOR_NAME = "SELECT * FROM book WHERE author=?"; //LIKE %?%";


    @Override
    public List<BookEntity> getBooks() {
        return jdbcTemplate.query(GET_BOOKS, new BeanPropertyRowMapper<>(BookEntity.class));
    }


    @Override
    public void saveBook(BookEntity bookEntity) {
        jdbcTemplate.update(ADD_BOOK, bookEntity.getISBN(), bookEntity.getTitle(), bookEntity.getAuthor(),
                bookEntity.getNumberOfPages(), bookEntity.getWeight(), bookEntity.getPrice());
    }


    @Override
    public void updateBook(BookEntity bookEntity) {
        jdbcTemplate.update(UPDATE_BOOK, bookEntity.getISBN(), bookEntity.getTitle(), bookEntity.getAuthor(),
                bookEntity.getNumberOfPages(), bookEntity.getWeight(), bookEntity.getPrice(), bookEntity.getId());
    }


    @Override
    public void deleteBook(Long id) {
        jdbcTemplate.update(DELETE_BOOK, id);
    }


    // возвращает true если передаваемый id найден в базе
    @Override
    public boolean isContainsId(Long id) {

        List<BookEntity> bookEntityList = jdbcTemplate.query(FIND_BOOK_BY_ID,
                new BeanPropertyRowMapper<>(BookEntity.class), id);

        return !bookEntityList.isEmpty();
    }


    // возвращает True если ISBN передаваемой книги совпадает с ISBN книги уже имеющейся в базе
    @Override
    public boolean isContainsISBN(BookEntity bookEntity) {

        List<BookEntity> bookEntityList = jdbcTemplate.query(FIND_BOOK_BY_ISBN,
                new BeanPropertyRowMapper<>(BookEntity.class), bookEntity.getISBN());

        return !bookEntityList.isEmpty();
    }


    // возвращает True если ISBN книги book (передаваемой в параметре метода), совпадает с ISBN книги уже имеющейся
    // в каталоге (ситуация когда при редактировании книги, мы ввели ISBN который уже присвоен другой книге)
    @Override
    public boolean otherBookContainsISBN(BookEntity bookEntity) {

        List<BookEntity> bookEntityList = jdbcTemplate.query(FIND_OTHER_BOOK_WITH_ISBN,
                new BeanPropertyRowMapper<>(BookEntity.class), bookEntity.getISBN(), bookEntity.getId());

        return !bookEntityList.isEmpty();
    }

    public List<BookEntity> getBooksByAuthorName(String authorName) {

        return jdbcTemplate.query(FIND_BOOK_BY_AUTHOR_NAME,
                new BeanPropertyRowMapper<>(BookEntity.class), authorName);
    }
}
