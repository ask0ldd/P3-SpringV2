package com.example.immo.controllers;

import com.example.immo.dto.LoginDto;
import com.example.immo.dto.RegistrationDto;
import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.dto.responses.LoggedUserResponseDto;
import com.example.immo.dto.responses.TokenResponseDto;
import com.example.immo.models.User;
import com.example.immo.services.AuthService;
import com.example.immo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://editor.swagger.io"})
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> userRegister(@RequestBody RegistrationDto body) {
        try {
            // System.out.println("\u001B[31m" + "register" + "\u001B[0m");
            authService.registerUser(body.getEmail(), body.getUsername(), body.getPassword());
            TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
            if (token == null || token.toString().isEmpty())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<TokenResponseDto>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto body) {
        try {
            TokenResponseDto token = authService.loginUser(body.getEmail(), body.getPassword());
            if (Objects.equals(token.getToken(), "")) {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("error"),
                        HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<TokenResponseDto>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve the infos of the current authenticated User
    // https://www.baeldung.com/get-user-in-spring-security
    // https://www.baeldung.com/spring-security-map-authorities-jwt
    // https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-claims
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(Principal principal){
        try {
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);
            if (loggedUser == null) {
                return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<LoggedUserResponseDto>(new LoggedUserResponseDto(loggedUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
