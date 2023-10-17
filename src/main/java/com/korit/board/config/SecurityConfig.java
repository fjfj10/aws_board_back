package com.korit.board.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity  // 기존의 설정대신 이 시큐리티 정책을 따르겠다
@Configuration  // 설정을 IOC에 등록 @Configuration은 한번에 2개 이상의 component를 등록이 가능하다 => @Bean여러게 등록가능
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean   // @Bean으로 외부라이브러리에서 가지고 온 BCryptPasswordEncode를 passwordEncoder이름으로 IOC에 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();        // WebMvnConfig의 Cors 정책을 따른다
        http.csrf().disable();
        http.authorizeRequests()    // 모든 요청은 인증을 받는다
                .antMatchers("/auth/**")
                .permitAll();

    }
}
