package com.chibuisi.springsecapp.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountDTO {
    private String email;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
}
