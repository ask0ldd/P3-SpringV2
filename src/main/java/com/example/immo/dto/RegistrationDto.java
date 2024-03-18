package com.example.immo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationDto {
    private String username;
    private String email;
    private String password;

    public RegistrationDto(String email, String name, String password) {
        super();
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public String toString() {
        return "Registration infos = " + this.username + " : " + this.password;
    }

}