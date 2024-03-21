package com.example.immo.services.interfaces;

import com.example.immo.dto.responses.MessageResponseDto;
import com.example.immo.models.Message;

public interface IMessageService {
    Iterable<Message> getMessages();

    Iterable<MessageResponseDto> getReturnableMessages();

    Message getMessage(Integer id);

    MessageResponseDto getReturnableMessage(Integer id);

    Message saveMessage(Message message);

    void deleteMessage(Integer id);
}