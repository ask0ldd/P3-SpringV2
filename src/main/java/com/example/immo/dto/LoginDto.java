package com.example.immo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    private String email;
    private String password;

    public LoginDto(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

}
