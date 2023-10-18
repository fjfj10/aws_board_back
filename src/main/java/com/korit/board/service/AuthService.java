package com.korit.board.service;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.DuplicateException;
import com.korit.board.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Boolean signup(SignupReqDto signupReqDto) {

        User user = signupReqDto.toUserEntity(passwordEncoder);

        int errorCode = userMapper.checkDuplicate(user);
        if (errorCode > 0) {
            responseDuplicateError(errorCode);
        }

        return userMapper.saveUser(user) > 0;
    }

    private void responseDuplicateError(int errorCode) {
        Map<String, String> errorMap = new HashMap<>();

        switch (errorCode) {
            case 1:
                errorMap.put("email", "이미 사용중인 이메일 입니다.");
                break;
            case 2:
                errorMap.put("nickname", "이미 사용중인 닉네임 입니다.");
                break;
            case 3:
                errorMap.put("email", "이미 사용중인 이메일 입니다.");
                errorMap.put("nickname", "이미 사용중인 닉네임 입니다.");
                break;
        }
        throw new DuplicateException(errorMap);
    }
}
