package com.korit.board.service;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.aop.annotation.ReturnAop;
import com.korit.board.dto.SigninReqDto;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.DuplicateException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    // AuthenticationManagerBuilder를 바로 사용하지 않고 PrincipalProvider로 직접 만들어 사용
    private final PrincipalProvider principalProvider;
    private final JwtProvider jwtProvider;

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

    @ReturnAop
    public String signin(SigninReqDto signinReqDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signinReqDto.getEmail(), signinReqDto.getPassword());

        Authentication authentication = principalProvider.authenticate(authenticationToken);
        String accessToken = jwtProvider.generateToken(authentication);

        return accessToken;
    }
}
