package com.korit.board.security.oauth2;

import com.korit.board.entity.User;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String oauth2Id = authentication.getName();
        User user = userMapper.findUserByOauth2Id(oauth2Id);

        // 회원가입이 안됐을때 OAuth2 계정 회원가입 페이지로 이동, Redirect는 무조건 get요청 json으로 정보 넘기기 불가능 -> 주소에 넣는다
        if (user == null) {
            DefaultOAuth2User defaultOAuth2User =  (DefaultOAuth2User) authentication.getPrincipal();
            String name = defaultOAuth2User.getAttributes().get("name").toString();
            String profileImage = defaultOAuth2User.getAttributes().get("profile_image").toString();
            String provider = defaultOAuth2User.getAttributes().get("provider").toString();

            response.sendRedirect("http://localhost:3000/auth/oauth2/signup" +
                    "?oauth2Id=" + oauth2Id +
                    "&name=" + URLEncoder.encode(name, "UTF-8") +
                    "&profileImage=" + profileImage +
                    "&provider=" + provider);
            return;
        }
        // user가 있다면 토큰 발행
        PrincipalUser principalUser = new PrincipalUser(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
        String accessToken = jwtProvider.generateToken(authenticationToken);

        response.sendRedirect("http://localhost:3000/auth/oauth2/login" +
                "?token=" + accessToken);
    }
}
