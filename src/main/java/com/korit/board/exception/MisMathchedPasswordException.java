package com.korit.board.exception;

public class MisMathchedPasswordException extends RuntimeException{

    public MisMathchedPasswordException() {
        super("비밀번호가 서로 일치하지 않습니다.");
    }
}
