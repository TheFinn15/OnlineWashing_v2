package com.OnlineWashing.exception.historyTrans;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HistoryTransactionNotFoundController {
    @ExceptionHandler(value = HistoryTransactionNotFoundException.class)
    public ResponseEntity<Object> exception(HistoryTransactionNotFoundException ex) {
        return new ResponseEntity<>("History not found", HttpStatus.NOT_FOUND);
    }
}
