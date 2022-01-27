package com.self.taskintervale.demoREST.repository;

import com.self.taskintervale.demoREST.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class BooksRepositoryImplTest {

    private final BooksRepositoryImpl booksRepositoryImpl;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Autowired
    BooksRepositoryImplTest(BooksRepositoryImpl booksRepositoryImpl) {
        this.booksRepositoryImpl = booksRepositoryImpl;
    }



    BookEntity bookEntity = new BookEntity(10L, "9994567891333", "мурзилка", "Петров",
            140, 0.33, BigDecimal.valueOf(130.33));

    List<BookEntity> bookEntityList = List.of(
            new BookEntity(1L, "1114567891333", "мурзилка", "Иванов",
                    30, 0.33, BigDecimal.valueOf(130.33)),
            new BookEntity(2L, "2224567891333", "дачник", "Петров",
                    20, 0.23, BigDecimal.valueOf(70.33)),
            new BookEntity(3L, "3334567891333", "кроссворды", "Сидоров",
                    10, 0.13, BigDecimal.valueOf(30.33))
    );


    @BeforeEach
    public void setJdbcTemplateMock() {
        //MockitoAnnotations.initMocks(this);
        //MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(booksRepositoryImpl, "jdbcTemplate", jdbcTemplate);
    }


    @Test()
    void getBooksReturnBookEntityList() {

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(bookEntityList);
        assertEquals(bookEntityList, booksRepositoryImpl.getBooks());
    }

    @Test
    void saveBookInvokeJdbcTemplateSaveBook() {

        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyDouble(), any(BigDecimal.class))).thenReturn(1);
        booksRepositoryImpl.saveBook(bookEntity);
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyDouble(), any(BigDecimal.class));
    }

    @Test
    void updateBookInvokeJdbcTemplateUpdateBook() {

        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyDouble(), any(BigDecimal.class), anyInt())).thenReturn(1);
        booksRepositoryImpl.updateBook(bookEntity);
        verify(jdbcTemplate, times(1)).update(anyString(), anyString(), anyString(), anyString(),
                anyInt(), anyDouble(), any(BigDecimal.class), anyLong());
    }

    @Test
    void deleteBookInvokeJdbcTemplateDeleteBook() {

        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);
        booksRepositoryImpl.deleteBook(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    //***********************************************************************************************
    // Тестируем  метод      public boolean isContainsId(Long id)
    // возвращает true если передаваемый id найден в базе
    //***********************************************************************************************

    @Test
    void isContainsIdInDataBase() {
        //Проверяется ситуация когда когда искомый id найден в базе
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyLong())).thenReturn(bookEntityList);
        assertTrue(booksRepositoryImpl.isContainsId(1L));
    }

    @Test
    void isContainsIdNotInDataBase() {
        //Проверяется ситуация когда когда искомый id не найден в базе
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyLong())).
                thenReturn(Collections.emptyList());
        assertFalse(booksRepositoryImpl.isContainsId(1L));
    }

    //***********************************************************************************************
    // Тестируем  метод      public boolean isContainsISBN(BookEntity bookEntity)
    // возвращает True если ISBN передаваемой книги совпадает с ISBN книги уже имеющейся в базе
    //***********************************************************************************************

    @Test
    void isContainsISBNInDataBase() {
        //Проверяется ситуация когда когда искомый ISBN найден в базе
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyString())).thenReturn(bookEntityList);
        assertTrue(booksRepositoryImpl.isContainsISBN(bookEntity));
    }

    @Test
    void isContainsISBNNotInDataBase() {
        //Проверяется ситуация когда когда искомый ISBN в базе не найден
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyString())).
                thenReturn(Collections.emptyList());
        assertFalse(booksRepositoryImpl.isContainsISBN(bookEntity));
    }

    //***********************************************************************************************
    // Тестируем  метод      otherBookContainsISBN(BookEntity bookEntity)
    // возвращает True если ISBN книги book (передаваемой в параметре метода), совпадает с ISBN книги уже имеющейся
    // в каталоге (ситуация когда при редактировании книги, мы ввели ISBN который уже присвоен другой книге)
    //***********************************************************************************************

    @Test
    void otherBookContainsISBNAssignedToAnotherBook() {
        //Проверяется ситуация когда когда искомый ISBN найден в базе (искомый ISBN уже присвоен другой книге)
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyString(), anyLong())).
                                                                                        thenReturn(bookEntityList);
        assertTrue(booksRepositoryImpl.otherBookContainsISBN(bookEntity));
    }

    @Test
    void otherBookContainsISBNNotAssignedToAnotherBook() {
        //Проверяется ситуация когда когда ISBN не найден в базе
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), anyString(), anyLong())).
                                                                                thenReturn(Collections.emptyList());
        assertFalse(booksRepositoryImpl.otherBookContainsISBN(bookEntity));
    }

}