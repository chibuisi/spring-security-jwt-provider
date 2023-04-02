package com.chibuisi.springsecapp.service;

import com.chibuisi.springsecapp.model.Role;
import com.chibuisi.springsecapp.model.UserAccount;
import com.chibuisi.springsecapp.model.UserRole;
import com.chibuisi.springsecapp.user.UserAccountDTO;
import com.chibuisi.springsecapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chibuisi.springsecapp.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class MyUserDetailsService implements UserDetailsService {
	private Logger log = Logger.getLogger("HomeResource");
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

		Optional<UserAccount> optionalUserAccount;

		optionalUserAccount = userRepository.findUserAccountByUsername(usernameOrEmail);
		if(!optionalUserAccount.isPresent())
			optionalUserAccount = userRepository.findUserAccountByEmail(usernameOrEmail);
		if(!optionalUserAccount.isPresent())
			throw new UsernameNotFoundException("User " + usernameOrEmail + " not Found");
		UserAccount userAccount = optionalUserAccount.get();

	    return org.springframework.security.core.userdetails.User
	        .withUsername(usernameOrEmail)
	        .password(userAccount.getPassword())
	        .authorities(userAccount.getRoles())
	        .accountExpired(false)
	        .accountLocked(false)
	        .credentialsExpired(false)
	        .disabled(false)
	        .build();
	}
	public UserDetails loadUserByUsernameOrEmailAndPassword(String usernameOrEmail){
		Optional<UserAccount> optionalUserAccount;

		optionalUserAccount = userRepository.findUserAccountByUsername(usernameOrEmail);
		if(!optionalUserAccount.isPresent())
			optionalUserAccount = userRepository.findUserAccountByEmail(usernameOrEmail);
		if(!optionalUserAccount.isPresent())
			throw new UsernameNotFoundException("User " + usernameOrEmail + " not Found");
		UserAccount userAccount = optionalUserAccount.get();
		return org.springframework.security.core.userdetails.User
				.withUsername(usernameOrEmail)
				.password(passwordEncoder.encode(userAccount.getPassword()))
				.authorities(userAccount.getRoles())
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}

	public UserAccountDTO saveUserAccount(UserAccountDTO userAccountDTO){
		// todo validate userAccount against null values
		Optional<UserAccount> existing = userRepository.findUserAccountByEmail(userAccountDTO.getEmail());
		if(!existing.isPresent())
			existing = userRepository.findUserAccountByUsername(userAccountDTO.getUsername());
		if(existing.isPresent())
			//todo handle this exception in a controller advice
			throw new RuntimeException("User Already Exists");
		UserAccount userAccount = UserAccount.builder()
				.email(userAccountDTO.getEmail())
				.username(userAccountDTO.getUsername())
				.firstName(userAccountDTO.getFirstname())
				.lastName(userAccountDTO.getLastname())
				.password(passwordEncoder.encode(userAccountDTO.getPassword()))
				.build();
		UserRole userRole = UserRole.builder().roleName(Role.ROLE_USER).build();
		userAccount.setRoles(Collections.singletonList(userRole));
		userRepository.save(userAccount);
		return userAccountDTO;
	}

	@Deprecated
	public Optional<UserAccountDTO> findUserByCredentials(String email, String username, String password){
		UserAccount userAccount;
		Optional<UserAccount> optionalExisting = userRepository
				.findUserAccountByEmail(email);
		if(!optionalExisting.isPresent())
			optionalExisting = userRepository
					.findUserAccountByUsername(username);
		if (!optionalExisting.isPresent())
			return Optional.empty();
		userAccount = optionalExisting.get();
		UserAccountDTO userAccountDTO = UserAccountDTO.builder()
				.email(userAccount.getEmail())
				.username(userAccount.getUsername())
				.firstname(userAccount.getFirstName())
				.lastname(userAccount.getLastName()).build();
		return Optional.of(userAccountDTO);
	}
}
