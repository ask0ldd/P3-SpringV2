package com.example.immo.dto.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDto {

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty
    @Size(min=3, max=32, message="The password must be between {min} and {max} characters long")
    private String password;

    public LoginDto(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

}
