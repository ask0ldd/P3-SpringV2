package com.example.immo.dto.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PayloadLoginDto {

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty
    @Size(min=5, max=32, message="The password must be between {min} and {max} characters long")
    private String password;

    public PayloadLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
