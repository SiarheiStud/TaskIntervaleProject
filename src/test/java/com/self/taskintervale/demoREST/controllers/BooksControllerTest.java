package com.self.taskintervale.demoREST.controllers;

import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.entity.dto.BookListDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerTest {

    @Mock
    private BookService bookService;

    private final BooksController booksController;
    private final TestRestTemplate testRestTemplate;

    @Autowired
    BooksControllerTest(BooksController booksController, TestRestTemplate testRestTemplate) {
        this.booksController = booksController;
        this.testRestTemplate = testRestTemplate;
    }

    private final BookDTO bookDTO = new BookDTO("9994567891339", "мурзилка", "Петров",
            140, 0.33, BigDecimal.valueOf(130.33));

    private final List<BookDTO> bookDTOList = List.of(
            new BookDTO("1114567891333", "мурзилка", "Иванов",
                    30, 0.33, BigDecimal.valueOf(130.33)),
            new BookDTO("2224567891333", "дачник", "Петров",
                    20, 0.23, BigDecimal.valueOf(70.33)),
            new BookDTO("3334567891333", "кроссворды", "Сидоров",
                    10, 0.13, BigDecimal.valueOf(30.33)) );


    @BeforeEach
    public void setBookServiceMock() {
        ReflectionTestUtils.setField(booksController, "bookService", bookService);
    }


    @Test
    void getAllBooksReturnStatus201AndBookDTOList() {

        BookListDTO bookListDTO = new BookListDTO();
        bookListDTO.setBookDtoList(bookDTOList);
        when(bookService.getBooks()).thenReturn(bookListDTO);
        ResponseEntity<BookListDTO> response = testRestTemplate.exchange(
                "/books",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookListDTO, response.getBody());
        verify(bookService,times(1)).getBooks();
    }


    @Test
    void addBookReturnStatus201() throws ISBNAlreadyExistsException {

        doNothing().when(bookService).saveBook(bookDTO);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/books", bookDTO, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Книга сохранена", response.getBody());
        verify(bookService, times(1)).saveBook(any(BookDTO.class));
    }


    @Test
    void updateBookInfoReturnStatus200() throws ISBNAlreadyExistsException, BookNotFoundException {

        doNothing().when(bookService).updateBook(any(BookDTO.class), anyLong());
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookDTO);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/books/{id}",
                HttpMethod.PUT,
                httpEntity,
                String.class,
                900L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Изменения сохранены", response.getBody());
        verify(bookService, times(1)).updateBook(any(BookDTO.class), anyLong());
    }


    @Test
    void deleteBookReturnStatus200() throws BookNotFoundException {

        doNothing().when(bookService).deleteBook(anyLong());
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/books/{id}",
                HttpMethod.DELETE,
                null,
                String.class,
                5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Книга удалена", response.getBody());
        verify(bookService, times(1)).deleteBook(anyLong());
    }
}