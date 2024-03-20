package com.example.immo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.immo.dto.responses.MessageResponseDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Message;
import com.example.immo.repositories.MessageRepository;
import com.example.immo.services.interfaces.IMessageService;

@Service
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        return messages;
    }

    public Iterable<MessageResponseDto> getReturnableMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        Iterable<MessageResponseDto> returnableMessages = StreamSupport.stream(messages.spliterator(), false)
                .map(message -> {
                    return new MessageResponseDto(message);
                })
                .collect(Collectors.toList());
        return returnableMessages;
    }

    public Message getMessage(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
    }

    public MessageResponseDto getReturnableMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
        return new MessageResponseDto(message);
    }

    public Message saveMessage(Message message) {
        return Optional.of(messageRepository.save(message))
                .orElseThrow(() -> new RuntimeException("Failed to save the message."));
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
        messageRepository.delete(message);
    }
}