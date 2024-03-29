package com.example.immo.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TokenResponseDto {

    private String token;

    public TokenResponseDto(String token) {
        super();
        this.token = token;
    }

}