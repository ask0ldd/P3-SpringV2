package com.example.immo.dto.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PayloadRegistrationDto {

    @NotEmpty
    @Size(min=3, max=128, message="The username must be between {min} and {max} characters long")
    private String username;

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty
    @Size(min=8, max=32, message="The password must be between {min} and {max} characters long")
    private String password;

    public PayloadRegistrationDto(String email, String name, String password) {
        super();
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public String toString() {
        return "Registration infos = " + this.username + " : " + this.password;
    }

}