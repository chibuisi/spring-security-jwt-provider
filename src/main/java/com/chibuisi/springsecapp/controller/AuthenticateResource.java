package com.chibuisi.springsecapp.controller;

import com.chibuisi.springsecapp.model.AuthenticationRequest;
import com.chibuisi.springsecapp.model.AuthenticationResponse;
import com.chibuisi.springsecapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticateResource {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        String jwt = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/authenticate")
    public String validateAuthentication(@RequestParam String jwt){
        return authenticationService.verify(jwt);
    }

}
