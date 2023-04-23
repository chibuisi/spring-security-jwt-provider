package com.chibuisi.springsecapp.user;

import com.chibuisi.springsecapp.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @PostMapping
    public UserAccountDTO saveUserAccount(@RequestBody UserAccountDTO userAccountDTO){
        return myUserDetailsService.saveUserAccount(userAccountDTO);
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam(required = false) String user){
        Optional<UserAccountDTO> userAccountDTO = myUserDetailsService.findUserByCredentials(user);
        if(!userAccountDTO.isPresent())
            return new ResponseEntity("Not Found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity(userAccountDTO, HttpStatus.OK);
    }

    @GetMapping("/role")
    public void getUserRoles(){
        
    }

    @PostMapping("/role")
    public void addRole(){

    }
}
