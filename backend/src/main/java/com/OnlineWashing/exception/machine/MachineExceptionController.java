package com.OnlineWashing.exception.machine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MachineExceptionController {
    @ExceptionHandler(value = MachineNotFoundException.class)
    public ResponseEntity<Object> exception(MachineNotFoundException ex) {
        return new ResponseEntity<>("Machine not found", HttpStatus.NOT_FOUND);
    }
}
