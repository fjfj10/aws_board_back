package com.korit.board.exception;

import lombok.Getter;

import java.util.Map;

public class AuthMailException extends RuntimeException{
    private Map<String, String> errorMap;

    public AuthMailException(String message) {
        super(message);
    }
}
