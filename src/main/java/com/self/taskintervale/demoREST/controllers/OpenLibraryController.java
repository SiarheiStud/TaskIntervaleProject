package com.self.taskintervale.demoREST.controllers;

import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.external.openlibrary.OpenLibraryBookDTO;
import com.self.taskintervale.demoREST.exeptions.OpenLibraryException;
import com.self.taskintervale.demoREST.external.openlibrary.OpenLibraryService;
import com.self.taskintervale.demoREST.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//Для swagger-а описание при генерации
@Tag(name = "OpenLibraryController", description = "отображены запросы контроллера")
@RestController
public class OpenLibraryController {

    private final OpenLibraryService openLibraryService;
    private final BookService bookService;

    public OpenLibraryController(OpenLibraryService openLibraryService, BookService bookService) {
        this.openLibraryService = openLibraryService;
        this.bookService = bookService;
    }

    @GetMapping("/books/{authorName}")
    public ResponseEntity<List<OpenLibraryBookDTO>> getBookByAuthorName(@PathVariable String authorName)
            throws OpenLibraryException {

        //Получаем книги автора из локальной базы данных
        List<BookEntity> booksEntityFromDB = bookService.getBooksByAuthorName(authorName);

        return new ResponseEntity<>(openLibraryService.getBooksByAuthorName(authorName, booksEntityFromDB),
                HttpStatus.OK);
    }
}
