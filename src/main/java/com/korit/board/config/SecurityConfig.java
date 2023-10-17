package com.korit.board.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity  // 기존의 설정대신 이 시큐리티 정책을 따르겠다
@Configuration  // 설정을 IOC에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();    // WebMvnConfig의 Cors 정책을 따른다
        http.csrf().disable();
        http.authorizeRequests()    // 모든 요청은 인증을 받는다
                .antMatchers("/auth/**")
                .permitAll();

    }
}
