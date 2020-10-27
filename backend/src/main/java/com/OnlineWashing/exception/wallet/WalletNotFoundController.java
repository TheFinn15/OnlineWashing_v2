package com.OnlineWashing.exception.wallet;

import com.OnlineWashing.exception.stock.StockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletNotFoundController {
    @ExceptionHandler(value = WalletNotFoundException.class)
    public ResponseEntity<Object> exception(WalletNotFoundException ex) {
        return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
    }
}
