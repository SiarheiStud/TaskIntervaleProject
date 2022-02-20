package com.self.taskintervale.demoREST.controllers;

import com.self.taskintervale.demoREST.external.openlibrary.OpenLibraryBookDTO;
import com.self.taskintervale.demoREST.external.openlibrary.OpenLibraryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OpenLibraryController(OpenLibraryService openLibraryService) {
        this.openLibraryService = openLibraryService;
    }

    @GetMapping("/books/{authorName}")
    public ResponseEntity<List<OpenLibraryBookDTO>> getBookByAuthorName(@PathVariable String authorName) {

        return new ResponseEntity<>(openLibraryService.getBooksByAuthorName(authorName), HttpStatus.OK);
    }
}
