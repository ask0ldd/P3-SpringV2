package com.example.immo.services;

import java.util.Optional;

import com.example.immo.exceptions.MessageNotFoundException;
import org.springframework.stereotype.Service;

import com.example.immo.models.Message;
import com.example.immo.repositories.MessageRepository;
import com.example.immo.services.interfaces.IMessageService;

@Service
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {
        return Optional.of(messageRepository.save(message))
                .orElseThrow(() -> new RuntimeException("Failed to save the message."));
    }

    // useless
    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    // useless
    public Message getMessage(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Target message cannot be found."));
    }

    // useless
    public void deleteMessage(Integer id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Target message cannot be found."));
        messageRepository.delete(message);
    }
}