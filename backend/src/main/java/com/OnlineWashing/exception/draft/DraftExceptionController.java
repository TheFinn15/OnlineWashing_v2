package com.OnlineWashing.exception.draft;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DraftExceptionController {
    @ExceptionHandler(value = DraftNotFoundException.class)
    public ResponseEntity<Object> exception(DraftNotFoundException ex) {
        return new ResponseEntity<>("Draft not found", HttpStatus.NOT_FOUND);
    }
}
