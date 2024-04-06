package com.example.immo.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.immo.models.Role;
import com.example.immo.models.User;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.repositories.UserRepository;
import com.example.immo.services.interfaces.IAuthService;

@Service
@Transactional
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public String registerUser(String email, String username, String password) {
        try {
            // the password of the user being registered has to be encoded before its insertion into the DB
            String encodedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            userRepository.save(new User(null, username, email,
                    encodedPassword, authorities));

            // try to authenticate the user using email and password
            // Authentication : Set by an AuthenticationManager to indicate the authorities that the principal has been granted
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // produces a JWT based on the Authentication produced for the authenticated user
            return tokenService.generateJwt(auth);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    public String loginUser(String email, String password) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return tokenService.generateJwt(auth);
        } catch (AuthenticationException e) {
            return null;
        }
    }
}