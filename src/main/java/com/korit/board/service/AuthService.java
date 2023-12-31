package com.korit.board.service;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.aop.annotation.ReturnAop;
import com.korit.board.dto.MergeOauthReqDto;
import com.korit.board.dto.SigninReqDto;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.DuplicateException;
import com.korit.board.exception.MisMathchedPasswordException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
import org.springframework.transaction.annotation.Transactional;

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

    public boolean authenticate(String token) {
        Claims claims = jwtProvider.getClaims(token);
        if (claims == null) {
            throw new JwtException("인증 토큰 유효성 검사 실패");
        }
        return Boolean.parseBoolean(claims.get("enabled").toString());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean mergeOauth2(MergeOauthReqDto mergeOauthReqDto) {
        User user = userMapper.findUserByEmail(mergeOauthReqDto.getEmail());

        if(!passwordEncoder.matches(mergeOauthReqDto.getPassword(), user.getPassword())) {
            // 비밀번호 일치하지 않을 시
//            throw new MisMathchedPasswordException();     // badRequest
            throw new BadCredentialsException("BadCredentials");    // UNAUTHORIZED
        }

        return userMapper.updateOauth2IdAndProvider(mergeOauthReqDto.toUserEntity()) > 0;
    }
}
