package com.korit.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korit.board.aop.annotation.TimeAop;
import com.korit.board.exception.AuthMailException;
import com.korit.board.exception.DuplicateException;
import com.korit.board.exception.MisMathchedPasswordException;
import com.korit.board.exception.ValidException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        Map<String, String> message = new HashMap<>();
        message.put("authError", "사용자 정보를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException badCredentialsException) {
        Map<String, String> message = new HashMap<>();
        message.put("authError", "사용자 정보를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledException(DisabledException disabledException) {
        Map<String, String> message = new HashMap<>();
        message.put("disabled", "이메일 인증이 필요합니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> jwtException(JwtException jwtException) {
        Map<String, String> message = new HashMap<>();
        message.put("jwt", "인증이 유효하지 않습니다..");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(AuthMailException.class)
    public ResponseEntity<?> authMailException(AuthMailException mailException) {
        Map<String, String> message = new HashMap<>();
        message.put("authMail", mailException.getMessage());
        return ResponseEntity.ok().body(message);
    }

    @ExceptionHandler(MisMathchedPasswordException.class)
    public ResponseEntity<?> misMathchedPasswordException(MisMathchedPasswordException misMathchedPasswordException) {
        Map<String, String> message = new HashMap<>();
        message.put("misMathched", misMathchedPasswordException.getMessage());
        return ResponseEntity.badRequest().body(message);
    }
}
