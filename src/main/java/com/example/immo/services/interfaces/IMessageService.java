package com.example.immo.services.interfaces;

import com.example.immo.models.Message;

public interface IMessageService {
    Iterable<Message> getMessages();
    Message getMessage(Integer id);
    Message saveMessage(Message message);
    void deleteMessage(Integer id);
}