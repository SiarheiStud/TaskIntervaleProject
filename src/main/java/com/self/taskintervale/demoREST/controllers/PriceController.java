package com.self.taskintervale.demoREST.controllers;


import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.external.bank.BankDTO;
import com.self.taskintervale.demoREST.external.bank.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@Validated
@RestController
public class PriceController {

    private final BankService bankService;

    @Autowired
    public PriceController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/price/{title}")       //  localhost:8080/price/{title}
    public ResponseEntity<List<BankDTO>> getPrice(@PathVariable @NotBlank(message = "В запросе title книги " +
            "(название книги) не может быть пустым ," +
            "либо состоять из одних пробелов") String title) throws BookNotFoundException {

        return new ResponseEntity<>(bankService.getPrice(title), HttpStatus.OK);
    }
}
