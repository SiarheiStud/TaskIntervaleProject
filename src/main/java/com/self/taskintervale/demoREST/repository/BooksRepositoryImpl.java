package com.self.taskintervale.demoREST.repository;

import com.self.taskintervale.demoREST.entity.BookEntity;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Component
public class BooksRepositoryImpl implements BookRepository{

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


    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/book_db",
                "postgres",
                "postgres");
    }


    @Override
    public List<BookEntity> getBooks() {

        List<BookEntity> booksList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(GET_BOOKS)) {

            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String ISBN = resultSet.getString("ISBN");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int numberOfPages = resultSet.getInt("numberOfPages");
                double weight = resultSet.getDouble("weight");
                BigDecimal price = resultSet.getBigDecimal("price");

                BookEntity bookEntity = new BookEntity(id, ISBN, title, author, numberOfPages, weight, price);
                booksList.add(bookEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booksList;
    }


    @Override
    public void saveBook(BookEntity bookEntity) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(ADD_BOOK)) {

            pStatement.setString(1, bookEntity.getISBN());
            pStatement.setString(2, bookEntity.getTitle());
            pStatement.setString(3, bookEntity.getAuthor());
            pStatement.setInt(4, bookEntity.getNumberOfPages());
            pStatement.setDouble(5, bookEntity.getWeight());
            pStatement.setBigDecimal(6, bookEntity.getPrice());
            pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateBook(BookEntity bookEntity) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(UPDATE_BOOK)) {

            pStatement.setString(1, bookEntity.getISBN());
            pStatement.setString(2, bookEntity.getTitle());
            pStatement.setString(3, bookEntity.getAuthor());
            pStatement.setInt(4, bookEntity.getNumberOfPages());
            pStatement.setDouble(5, bookEntity.getWeight());
            pStatement.setBigDecimal(6, bookEntity.getPrice());
            pStatement.setLong(7, bookEntity.getId());
            pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteBook(Long id) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(DELETE_BOOK)) {

            pStatement.setLong(1, id);
            pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // возвращает true если передаваемый id найден в базе
    @Override
    public boolean isContainsId(Long id) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(FIND_BOOK_BY_ID)) {

            pStatement.setLong(1, id);
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet.next()) { // Если в resultSet есть содержимое, значит книга по id найдена
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Книги с запрашиваемым id нет в базе
    }


    // возвращает True если ISBN передаваемой книги совпадает с ISBN книги уже имеющейся в базе
    @Override
    public boolean isContainsISBN(BookEntity bookEntity) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(FIND_BOOK_BY_ISBN)) {

            pStatement.setString(1, bookEntity.getISBN());
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet.next()) { //Если в resultSet есть содержимое, значит ISBN занят другой книгой
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; //Если запрос ничего не вернул, значит ISBN свободный (в базе нет книги с таким ISBN)
    }


    // возвращает True если ISBN книги book (передаваемой в параметре метода), совпадает с ISBN книги уже имеющейся
    // в каталоге (ситуация когда при редактировании книги, мы ввели ISBN который уже присвоен другой книге)
    @Override
    public boolean otherBookContainsISBN(BookEntity bookEntity) {

        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(FIND_OTHER_BOOK_WITH_ISBN)) {

            pStatement.setString(1, bookEntity.getISBN());
            pStatement.setLong(2, bookEntity.getId());
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet.next()) { //Если в resultSet есть содержимое, значит ISBN занят другой книгой
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; //Если запрос ничего не вернул, значит ISBN свободный (в базе нет книги с таким ISBN)
    }

}
