package com.example.immo.dto.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PayloadMessageDto {
    private Integer rental_id;
    private Integer user_id;
    private String message;
}