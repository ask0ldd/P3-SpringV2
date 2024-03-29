package com.example.immo.dto.responses;

import com.example.immo.models.Message;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;

@Getter
@Setter
@Data
public class MessageResponseDto {
    private Integer messageId;
    private RentalResponseDto rental;
    private UserResponseDto user;
    private String message;

    public MessageResponseDto(Message message) throws ParseException {
        super();
        this.messageId = message.getMessageId();
        this.message = message.getMessage();
        this.user = new UserResponseDto(message.getUser());
        this.rental = new RentalResponseDto(message.getRental());
    }
}
