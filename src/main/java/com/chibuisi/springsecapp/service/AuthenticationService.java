package com.chibuisi.springsecapp.service;

import com.chibuisi.springsecapp.model.AuthenticationRequest;
import com.chibuisi.springsecapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class AuthenticationService {
    private Logger log = Logger.getLogger("HomeResource");
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public String authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.info("Authentication processing failed");
            throw new Exception("invalid Username or Password",e);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return jwt;
    }

    public String verify(String jwt){
        boolean res = jwtUtil.verify(jwt);
        String username = null;
        try{
            username = jwtUtil.extractUsername(jwt);
        }
        catch (Exception e){
            log.info("JWT verification is failed: "+ e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if(res)
            return "OK";
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
