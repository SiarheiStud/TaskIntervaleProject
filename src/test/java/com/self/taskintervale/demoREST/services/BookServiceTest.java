package com.self.taskintervale.demoREST.services;

import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.entity.dto.BookListDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BooksRepositoryImpl booksRepositoryImpl;

    BookEntity bookEntity = new BookEntity(10L, "9994567891333", "мурзилка", "Петров",
            140, 0.33, BigDecimal.valueOf(130.33));

    BookDTO bookDTO = new BookDTO("9994567891333", "мурзилка", "Петров",
            140, 0.33, BigDecimal.valueOf(130.33));

    List<BookEntity> bookEntityList = List.of(
            new BookEntity(1L, "1114567891333", "мурзилка", "Иванов",
                    30, 0.33, BigDecimal.valueOf(130.33)),
            new BookEntity(2L, "2224567891333", "дачник", "Петров",
                    20, 0.23, BigDecimal.valueOf(70.33)),
            new BookEntity(3L, "3334567891333", "кроссворды", "Сидоров",
                    10, 0.13, BigDecimal.valueOf(30.33))
    );

    List<BookDTO> bookDTOList = List.of(
            new BookDTO("1114567891333", "мурзилка", "Иванов",
                    30, 0.33, BigDecimal.valueOf(130.33)),
            new BookDTO("2224567891333", "дачник", "Петров",
                    20, 0.23, BigDecimal.valueOf(70.33)),
            new BookDTO("3334567891333", "кроссворды", "Сидоров",
                    10, 0.13, BigDecimal.valueOf(30.33))
    );

    @Test
    void bookEntityIntoDTO() {

        BookDTO testBookDTO = BookService.bookEntityIntoDTO(bookEntity);

        assertEquals(bookEntity.getISBN(), testBookDTO.getISBN());
        assertEquals(bookEntity.getTitle(), testBookDTO.getTitle());
        assertEquals(bookEntity.getAuthor(), testBookDTO.getAuthor());
        assertEquals(bookEntity.getNumberOfPages(), testBookDTO.getNumberOfPages());
        assertEquals(bookEntity.getWeight(), testBookDTO.getWeight());
        assertEquals(bookEntity.getPrice(), testBookDTO.getPrice());
    }

    @Test
    void bookDTOIntoEntity() {

        BookEntity testBookEntity = BookService.bookDTOIntoEntity(bookDTO);

        assertNull(testBookEntity.getId());
        assertEquals(bookDTO.getISBN(), testBookEntity.getISBN());
        assertEquals(bookDTO.getTitle(), testBookEntity.getTitle());
        assertEquals(bookDTO.getAuthor(), testBookEntity.getAuthor());
        assertEquals(bookDTO.getNumberOfPages(), testBookEntity.getNumberOfPages());
        assertEquals(bookDTO.getWeight(), testBookEntity.getWeight());
        assertEquals(bookDTO.getPrice(), testBookEntity.getPrice());
    }

    @Test
    void getBooksReturnBookListDTO() {

        BookListDTO bookList = new BookListDTO();
        bookList.setBookDtoList(bookDTOList);
        when(booksRepositoryImpl.getBooks()).thenReturn(bookEntityList);
        BookListDTO testBookDTOList = bookService.getBooks();

        verify(booksRepositoryImpl, times(1)).getBooks();
        assertEquals(bookList, testBookDTOList);
    }

    //***********************************************************************************************
    // Тестируем  метод     public void saveBook(BookDTO bookDTO) throws ISBNAlreadyExistsException {
    //***********************************************************************************************
    @Test()
    void saveBookThrowException() {

        when(booksRepositoryImpl.isContainsISBN(any(BookEntity.class))).thenReturn(true);

        //Проверяем генерацию исключения (исключение должно быть сгенерировано)
        ISBNAlreadyExistsException thrownException = Assertions.assertThrows(
                ISBNAlreadyExistsException.class,
                () -> bookService.saveBook(bookDTO),
                "Тест провален, исключение не сгенерировано!"
        );
        String message = "Не корректный ISBN! Книга с ISBN = " + bookDTO.getISBN() + " уже содержится в каталоге.";

        //Проверяем сообщение сгенерированного исключения
        assertEquals(message, thrownException.getMessage());

        //Проверяем чтобы метод booksRepositoryImpl.saveBook(bookEntity) не вызывался
        verify(booksRepositoryImpl, never()).saveBook(any(BookEntity.class));
    }

    @Test()
    void saveBookComplete() {

        when(booksRepositoryImpl.isContainsISBN(any(BookEntity.class))).thenReturn(false);

        //Проверяем чтобы исключение не генерировалось
        Assertions.assertDoesNotThrow(
                () -> bookService.saveBook(bookDTO),
                "Тест провален! Было сгенерировано исключение!");

        //Проверяем чтобы метод booksRepositoryImpl.saveBook(bookEntity) вызвался
        verify(booksRepositoryImpl, times(1)).saveBook(any(BookEntity.class));
    }

    //***********************************************************************************************
    // Тестируем  метод      public void updateBook(BookDTO bookDTO, Long id) throws ISBNAlreadyExistsException,
    //                                                                               BookNotFoundException
    //***********************************************************************************************
    @Test
    void updateBookThrowISBNAlreadyExistsException() {
        //результат работы метода booksRepositoryImpl.isContainsId  не должен повлиять на результат теста,
        //поэтому возвращаем true
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(true);


        //при true - исключение должно быть сгенерировано, при false - исключение не генерируется
        when(booksRepositoryImpl.otherBookContainsISBN(any(BookEntity.class))).thenReturn(true);

        //Проверяем генерацию исключения (исключение должно быть сгенерировано)
        ISBNAlreadyExistsException thrownException = Assertions.assertThrows(
                ISBNAlreadyExistsException.class,
                () -> bookService.updateBook(bookDTO, 1L),
                "Тест провален! Исключение не было сгенерировано!");

        //Проверяем чтобы метод booksRepositoryImpl.updateBook(bookEntity) не вызывался
        verify(booksRepositoryImpl, never()).updateBook(any(BookEntity.class));

    }

    @Test()
    void updateBookNotThrowISBNAlreadyExistsException() {
        //результат работы метода booksRepositoryImpl.isContainsId  не должен повлиять на результат теста,
        //поэтому возвращаем true
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(true);


        //при true - исключение должно быть сгенерировано, при false - исключение не генерируется
        when(booksRepositoryImpl.otherBookContainsISBN(any(BookEntity.class))).thenReturn(false);

        //Проверяем чтобы исключение не генерировалось
        Assertions.assertDoesNotThrow(
                () -> bookService.updateBook(bookDTO, 1L),
                "Тест провален! Было сгенерировано исключение!");
    }

    @Test
    void updateBookThrowBookNotFoundException() {
        //результат работы метода booksRepositoryImpl.otherBookContainsISBN(bookEntity)  не должен повлиять
        // на результат теста, поэтому возвращаем false
        when(booksRepositoryImpl.otherBookContainsISBN(any(BookEntity.class))).thenReturn(false);


        //при false - исключение должно быть сгенерировано, при true - исключение не генерируется
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(false);

        //Проверяем генерацию исключения (исключение должно быть сгенерировано)
        BookNotFoundException thrownException = Assertions.assertThrows(
                BookNotFoundException.class,
                () -> bookService.updateBook(bookDTO, 1L),
                "Тест провален! Исключение не было сгенерировано!");

        //Проверяем чтобы метод booksRepositoryImpl.updateBook(bookEntity) не вызывался
        verify(booksRepositoryImpl, never()).updateBook(any(BookEntity.class));

    }

    @Test()
    void updateBookNotThrowBookNotFoundException() {
        //результат работы метода booksRepositoryImpl.otherBookContainsISBN(bookEntity)  не должен повлиять
        // на результат теста, поэтому возвращаем false
        when(booksRepositoryImpl.otherBookContainsISBN(any(BookEntity.class))).thenReturn(false);


        //при false - исключение должно быть сгенерировано, при true - исключение не генерируется
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(true);

        //Проверяем чтобы исключение не генерировалось
        Assertions.assertDoesNotThrow(
                () -> bookService.updateBook(bookDTO, 1L),
                "Тест провален! Было сгенерировано исключение!");
    }

    @Test()
    void updateBookComplete() throws ISBNAlreadyExistsException, BookNotFoundException {

        when(booksRepositoryImpl.otherBookContainsISBN(any(BookEntity.class))).thenReturn(false);
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(true);
        bookService.updateBook(bookDTO, 1L);

        verify(booksRepositoryImpl, times(1)).updateBook(any(BookEntity.class));

    }

    //***********************************************************************************************
    // Тестируем  метод      public void deleteBook(Long id) throws BookNotFoundException
    //***********************************************************************************************

    @Test
    void deleteBookThrowBookNotFoundException() {
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(false);

        //Проверяем генерацию исключения (исключение должно быть сгенерировано)
        BookNotFoundException thrownException = Assertions.assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(1L),
                "Тест провален! Исключение не было сгенерировано!");

        //Проверяем чтобы метод booksRepositoryImpl.deleteBook(id) не вызывался
        verify(booksRepositoryImpl, never()).deleteBook(anyLong());
    }

    @Test
    void deleteBookNotThrowBookNotFoundException() {
        when(booksRepositoryImpl.isContainsId(anyLong())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> bookService.deleteBook(1L),
                "Тест провален! Было сгенерировано исключение!");

        //Проверяем чтобы метод booksRepositoryImpl.deleteBook(id) вызвался
        verify(booksRepositoryImpl, times(1)).deleteBook(anyLong());
    }

}