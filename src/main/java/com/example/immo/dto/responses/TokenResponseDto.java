package com.example.immo.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponseDto {

    private String token;

    public TokenResponseDto(String token) {
        super();
        this.token = token;
    }

}