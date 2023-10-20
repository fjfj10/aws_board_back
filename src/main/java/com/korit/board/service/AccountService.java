package com.korit.board.service;

import com.korit.board.dto.UpdateProfileImgDto;
import com.korit.board.entity.User;
import com.korit.board.exception.AuthMailException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    @Transactional(rollbackFor = Exception.class)
    public boolean authenticateMail(String token) {
        Claims claims = jwtProvider.getClaims(token);

        if (claims == null) {
            throw new AuthMailException("만료된 인증 요청입니다.");
        }

        String email = claims.get("email").toString();
        System.out.println(email);
        // 이미 인증된 유저인지 확인
        User user = userMapper.findUserByEmail(email);
        if (user.getEnabled() > 0) {
            throw new AuthMailException("이미 인증이 완료된 요청입니다.");
        }

        return userMapper.updateEnabledToEmail(email) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfileImg(UpdateProfileImgDto updateProfileImgDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = User.builder().email(email).profileUrl(updateProfileImgDto.getProfileUrl()).build();

        return  userMapper.updateProfileUrl(user) > 0;
    }
}
