package com.self.taskintervale.demoREST.controllers;

import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.entity.dto.BookListDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@Tag(name = "BooksController", description = "отображены запросы контроллера") //Для swagger-а описание при генерации
@Validated
@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService) {

        this.bookService = bookService;
    }

    @GetMapping()   //  localhost:8080/books   получить все книги
    public ResponseEntity<BookListDTO> getAllBooks() {

        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @PostMapping()   //   localhost:8080/books   добавить книгу
    public ResponseEntity<String> addBook(@Valid @RequestBody BookDTO bookDTO) throws ISBNAlreadyExistsException {

        bookService.saveBook(bookDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")   //   localhost:8080/books/3   обновить информацию о книге
    public ResponseEntity<String> updateBookInfo(@Valid @RequestBody BookDTO bookDTO,
                                                 @PathVariable @Min(value = 1, message = "Минимальное значение id = 1")
                                                 Long id) throws ISBNAlreadyExistsException, BookNotFoundException {

        bookService.updateBook(bookDTO, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")   //   localhost:8080/books/4   удалить книгу
    public ResponseEntity<String> deleteBook(@PathVariable @Min(value = 1, message = "Минимальное значение id = 1")
                                                     Long id) throws BookNotFoundException {

        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
