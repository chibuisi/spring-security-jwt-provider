package com.chibuisi.springsecapp.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chibuisi.springsecapp.model.AuthenticationRequest;
import com.chibuisi.springsecapp.model.AuthenticationResponse;
import com.chibuisi.springsecapp.service.MyUserDetailsService;
import com.chibuisi.springsecapp.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import sun.text.normalizer.ICUBinary.Authenticate;
@Slf4j
@RestController
public class HomeResource {
	
	private Logger log = Logger.getLogger("HomeResource");
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService MyUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@RequestMapping({"/","/index"})
	public String home() {
		return "Welcome";
	}
	
	@RequestMapping("/admin")
	public String admin() {
//		Log.info("Admin logged in");
		return "Welcome Admin";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "Welcome User";
	}
	
	@RequestMapping("/manager")
	public String manager() {
		return "Welcome Manager";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		log.info("Request recieved for authentication");
		try {
			log.info("Request processing");
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			log.info("Authentication processing failed");
			throw new Exception("invalid Username or Password",e);
		}
		
		final UserDetails userDetails = MyUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}


}
