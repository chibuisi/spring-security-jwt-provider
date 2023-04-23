package com.chibuisi.springsecapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chibuisi.springsecapp.model.ApiKeyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chibuisi.springsecapp.service.MyUserDetailsService;
import com.chibuisi.springsecapp.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private ApiKeyConfiguration apiKeyConfiguration;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		String requestPath = request.getRequestURI();
		final String API_KEY = apiKeyConfiguration.getApiKey();
		final String CLIENT_ID = apiKeyConfiguration.getApiClientIdUI();

		if(requestPath.startsWith("/api")) {
			String apiKey = request.getHeader("X-Api-Key");
			String clientId = request.getHeader("X-Client-Id");

			if (apiKey == null || clientId == null || !clientId.equals(CLIENT_ID) || !apiKey.equals(API_KEY)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}else {
			String jwt = null;
			String username = null;

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				jwt = authHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}

			if (username != null && SecurityContextHolder.getContext() != null) {
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken token =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}

			filterChain.doFilter(request, response);
		}
	}

}
