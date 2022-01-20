package com.self.taskintervale.demoREST.exeptions;

public class ISBNAlreadyExistsException extends Exception {
    public ISBNAlreadyExistsException(String message) {
        super(message);
    }
}
