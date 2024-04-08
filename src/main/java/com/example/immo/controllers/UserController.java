package com.example.immo.controllers;

import com.example.immo.dto.responses.UserResponseDto;
import com.example.immo.models.User;
import com.example.immo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retrieve the target User
    @GetMapping("/user/{id}")
    @Operation(summary = "Retrieve a target user", description = "Endpoint to retrieved a target user.", tags = {
            "User" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved.", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found."),
    })
    public ResponseEntity<?> getUser(@PathVariable("id") final Integer id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<UserResponseDto>(new UserResponseDto(user), HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            return new ResponseEntity<String>("Can't find the requested User.", HttpStatus.NOT_FOUND);
        }
    }
}
