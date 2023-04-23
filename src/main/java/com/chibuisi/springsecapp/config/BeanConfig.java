package com.chibuisi.springsecapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chibuisi.springsecapp.filter.JwtRequestFilter;
import com.chibuisi.springsecapp.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public JwtUtil getJwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public JwtRequestFilter getJwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
