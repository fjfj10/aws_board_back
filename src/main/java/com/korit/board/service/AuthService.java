package com.korit.board.service;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Boolean signup(SignupReqDto signupReqDto) {

        User user = signupReqDto.toUserEntity(passwordEncoder);

        return userMapper.saveUser(user) > 0;
    }
}
