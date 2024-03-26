package com.example.immo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    @Email(message = "Invalid email format")
    private String email;

    @Size(min=3, max=32, message="The password must be between {min} and {max} characters long")
    private String password;

    public LoginDto(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

}
