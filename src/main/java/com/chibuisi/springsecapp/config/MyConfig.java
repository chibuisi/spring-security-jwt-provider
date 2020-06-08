package com.chibuisi.springsecapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chibuisi.springsecapp.filter.JwtRequestFilter;
import com.chibuisi.springsecapp.util.JwtUtil;

@Configuration
public class MyConfig {
	
	@Bean
	public JwtUtil getJwtUtil() {
		return new JwtUtil();
	}

	@Bean
	public JwtRequestFilter getJwtRequestFilter() {
		return new JwtRequestFilter();
	}
}
