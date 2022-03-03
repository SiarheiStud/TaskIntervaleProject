package com.self.taskintervale.demoREST.controllers;


import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.exeptions.BankException;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.external.bank.BankDTO;
import com.self.taskintervale.demoREST.external.bank.BankService;
import com.self.taskintervale.demoREST.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@Validated
@RestController
public class BanksController {

    private final BankService bankService;
    private final BookService bookService;
    private final String ERROR_MESSAGE = "В запросе title книги (название книги) не может быть пустым, " +
            "либо состоять из одних пробелов";

    public BanksController(BankService bankService, BookService bookService) {
        this.bankService = bankService;
        this.bookService = bookService;
    }

    @GetMapping("/price/{title}")       //  localhost:8080/price/{title}
    public ResponseEntity<List<BankDTO>> getPrice(@PathVariable @NotBlank(message = ERROR_MESSAGE) String title)
            throws BookNotFoundException, BankException {

        List<BookEntity> bookEntityList = bookService.getBooksByTitle(title);

        return new ResponseEntity<>(bankService.getPrice(title, bookEntityList), HttpStatus.OK);
    }
}
