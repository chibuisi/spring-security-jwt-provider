package com.chibuisi.springsecapp.service;

import com.chibuisi.springsecapp.model.UserAccount;
import com.chibuisi.springsecapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chibuisi.springsecapp.repository.UserRepository;

import java.util.logging.Logger;


@Service
public class MyUserDetailsService implements UserDetailsService {
	private Logger log = Logger.getLogger("HomeResource");
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		final UserAccount userAccount = userRepository.findByUsername(username);

	    if (userAccount == null) {
	      throw new UsernameNotFoundException("User '" + username + "' not found");
	    }

	    return org.springframework.security.core.userdetails.User
	        .withUsername(username)
	        .password(userAccount.getPassword())
	        .authorities(userAccount.getRoles())
	        .accountExpired(false)
	        .accountLocked(false)
	        .credentialsExpired(false)
	        .disabled(false)
	        .build();
	    }

}
