package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.aop.annotation.ReturnAop;
import com.korit.board.aop.annotation.TimeAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.exception.ValidException;
import com.korit.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ArgAop
    @TimeAop
    @ValidAop
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {

        authService.signup(signupReqDto);

        return ResponseEntity.ok(true);
    }


}
