package com.korit.board.controller;

import com.korit.board.aop.annotation.TimeAop;
import com.korit.board.exception.DuplicateException;
import com.korit.board.exception.ValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<?> validException(ValidException validException) {
        System.out.println("Valid예외처리됨");
        return ResponseEntity.badRequest().body(validException.getErrorMap());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateException(DuplicateException duplicateException) {
        System.out.println("Duplicate예외처리됨");
        return ResponseEntity.badRequest().body(duplicateException.getErrorMap());
    }
}
