package com.example.immo.dto.responses;

import com.example.immo.models.Message;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MessageResponseDto {
    private Long messageId;
    private RentalResponseDto rental;
    private UserResponseDto user;
    private String message;

    public MessageResponseDto(Message message) {
        super();
        this.messageId = message.getMessageId();
        this.message = message.getMessage();
        this.user = new UserResponseDto(message.getUser());
        this.rental = new RentalResponseDto(message.getRental());
    }
}
