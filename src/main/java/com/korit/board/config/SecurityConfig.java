package com.korit.board.config;


import com.korit.board.filter.JwtAuthenticationFilter;
import com.korit.board.security.PrincipalEntryPoint;
import com.korit.board.security.oauth2.OAuth2SuccessHandler;
import com.korit.board.service.PrincipalUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity  // 기존의 설정대신 이 시큐리티 정책을 따르겠다
@Configuration  // 설정을 IOC에 등록 @Configuration은 한번에 2개 이상의 component를 등록이 가능하다 => @Bean여러게 등록가능
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalEntryPoint principalEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PrincipalUserDetailsService principalUserDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean   // @Bean으로 외부라이브러리에서 가지고 온 BCryptPasswordEncode를 passwordEncoder이름으로 IOC에 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();        // WebMvnConfig의 Cors 정책을 따른다
        http.csrf().disable();
        http.authorizeRequests()    // 모든 요청은 인증을 받는다
                .antMatchers("/board/content", "/board/like/**")
                .authenticated()
                .antMatchers("/auth/**", "/board/**", "/boards/**")
                .permitAll()
                .anyRequest()
                // SecurityContextHolder 안에 authentication 객체(jwtAuthenticationFilter에서 만듦)가 있냐 없냐만 판단 => 없으면 PrincipalEntryPoint 예외 넘긴다
                .authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(principalEntryPoint)
                .and()
                .oauth2Login()
                .loginPage("http://localhost:3000/auth/signin")
                .successHandler(oAuth2SuccessHandler)   // oAuth2를 리턴해서 authenticatin이 만들어 졌을때 동작   <ㄱ
                .userInfoEndpoint()     // controller 역할 -> principalUserDetailsService로 정보 넘김               ㅣ
                .userService(principalUserDetailsService);      // authenticatin 객체를 만들어준다  ㅡㅡㅡㅡㅡㅡㅡㅡㅣ

    }
}
