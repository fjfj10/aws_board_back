package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.PrincipalRespDto;
import com.korit.board.dto.UpdatePasswordReqDto;
import com.korit.board.dto.UpdateProfileImgDto;
import com.korit.board.entity.User;
import com.korit.board.security.PrincipalUser;
import com.korit.board.service.AccountService;
import com.korit.board.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final MailService mailService;
    private final AccountService accountService;

    @GetMapping("/account/princlpal")
    public ResponseEntity<?> getPrincipal() {
        PrincipalUser principalUser =
                (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  // getPrincipal = JwtProvider UsernamePasswordAuthenticationToken 만들때 넣은 PrincipalUser
        User user = principalUser.getUser();
        PrincipalRespDto principalRespDto = user.toPrincipalDto();
        return ResponseEntity.ok(principalRespDto);
    }

    @PostMapping("/account/mail/auth")
    public ResponseEntity<?> sendAuthenticationmail() {

        return ResponseEntity.ok(mailService.sendAuthMail());
    }

    @PutMapping("/account/profile/img")
    public ResponseEntity<?> updateProfileImg(@RequestBody UpdateProfileImgDto updateProfileImgDto) {

        return ResponseEntity.ok(accountService.updateProfileImg(updateProfileImgDto));
    }

    @ArgAop
    @ValidAop
    @PutMapping("/account/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordReqDto updatePasswordReqDto, BindingResult bindingResult) {

        return ResponseEntity.ok(accountService.updatePassword(updatePasswordReqDto));
    }
}
