package com.example.immo.controllers;

import com.example.immo.dto.payloads.PayloadMessageDto;
import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.models.Message;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.MessageService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = {"http://localhost:4200"})
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final RentalService rentalService;

    public MessageController(MessageService messageService, UserService userService, RentalService rentalService) {
        this.messageService = messageService;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    // Create a new Message
    @PostMapping("/messages")
    @Operation(summary = "Create a Rental",
            tags = {"Messages"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent with success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Message sent with success.\"}"), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request / Can't create the target Message.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Can't create the target Message.\"}"), mediaType = "application/json")),
    })
    public ResponseEntity<?> createMessage(@RequestBody PayloadMessageDto message, Principal principal) {
        try {
            // ignoring the user id from the request body and retrieving the authenticated user
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);
            Rental rental = rentalService.getRental(message.getRental_id());
            Message newMessage = Message.builder().message(message.getMessage()).user(loggedUser)
                    .rental(rental).build();
            messageService.saveMessage(newMessage);
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Message sent with success."),
                    HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't create the target Message."), HttpStatus.BAD_REQUEST);
        }
    }
}