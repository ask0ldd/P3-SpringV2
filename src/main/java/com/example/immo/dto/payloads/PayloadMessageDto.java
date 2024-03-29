package com.example.immo.dto.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PayloadMessageDto {
    private Integer rental_id;
    private Integer user_id;
    @NotEmpty
    @Size(max=255, message="A message can't exceed {max} characters")
    private String message;
}