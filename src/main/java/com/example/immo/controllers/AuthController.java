package com.example.immo.controllers;

import com.example.immo.dto.LoginDto;
import com.example.immo.dto.RegistrationDto;
import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.dto.responses.LoggedUserResponseDto;
import com.example.immo.dto.responses.TokenResponseDto;
import com.example.immo.models.User;
import com.example.immo.services.AuthService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Endpoint to register a new user.", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration", content = @Content(schema = @Schema(implementation = TokenResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(examples = @ExampleObject(value = "{ }"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = @ExampleObject(value = "{ }")))
    })
    public ResponseEntity<?> userRegister(@RequestBody RegistrationDto body) {
        try {
            String token = authService.registerUser(body.getEmail(), body.getUsername(), body.getPassword());
            if (token == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<TokenResponseDto>(new TokenResponseDto(token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Endpoint for user login", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = TokenResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<?> userLogin(@RequestBody LoginDto body) {
        try {
            String token = authService.loginUser(body.getEmail(), body.getPassword());
            if (token == null)
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("error"), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<TokenResponseDto>(new TokenResponseDto(token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve the infos of the current authenticated User
    // https://www.baeldung.com/get-user-in-spring-security
    // https://www.baeldung.com/spring-security-map-authorities-jwt
    // https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-claims
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get logged in user details", description = "Retrieves information about the currently logged in user.", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations retrieved", content = @Content(schema = @Schema(implementation = LoggedUserResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "User not Found", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<?> getLoggedUser(Principal principal){
        try {
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);
            if (loggedUser == null) {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("User not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<LoggedUserResponseDto>(new LoggedUserResponseDto(loggedUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
