package com.self.taskintervale.demoREST.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BooksExceptionHandler {

    // Отрабатывает при проблемах с ISBN
    @ExceptionHandler(value = {ISBNAlreadyExistsException.class})
    public ResponseEntity<Object> handlerISBNException(Exception e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e.getMessage(), status);
    }

    // Отрабатывает при проблемах с поиском книги в каталоге книг
    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<Object> handlerBookNotFoundException(Exception e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e.getMessage(), status);
    }

    // Отрабатывает при проблемах с валидацией в теле запроса
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> findErrors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            findErrors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(findErrors, status);
    }

    // Отрабатывает при проблемах с валидацией в переменных пути или параметрах запроса
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handlerConstraintViolationException(ConstraintViolationException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> findErrors = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> {
            String pathName = constraintViolation.getPropertyPath().toString();
            String errorMessage = constraintViolation.getMessage();
            findErrors.put(pathName, errorMessage);
        });
        return new ResponseEntity<>(findErrors, status);
    }

    // Отрабатывает при проблемах с openlibrary
    @ExceptionHandler(value = {OpenLibraryException.class})
    public ResponseEntity<String> handlerOpenLibraryException(OpenLibraryException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e.getMessage(), status);
    }

    // Отрабатывает при проблемах с ibapi.alfabank.by:8273
    @ExceptionHandler(value = {BankException.class})
    public ResponseEntity<String> handlerBankException(BankException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e.getMessage(), status);
    }
}
