package com.example.immo.services;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.User;
import com.example.immo.repositories.UserRepository;

import lombok.Data;

@Data
@Service
/*
 * @Service : tout comme l’annotation @Repository, c’est une spécialisation
 * de @Component. Son rôle est donc le même, mais son nom a une valeur
 * sémantique pour ceux qui lisent votre code.
 */
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // mandatory to implement UserDetailsService
    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be found."));
    }

    public User getUser(final Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be found."));
    }

    public Iterable<User> getUsers() {
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext())
            throw new UserNotFoundException("No user can be found.");
        return users;
    }

    public void deleteUser(final Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target user cannot be deleted."));
        userRepository.delete(user);
    }

    public User saveUser(User user) {
        return Optional.of(userRepository.save(user))
                .orElseThrow(() -> new RuntimeException("Failed to save the User."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not valid."));
    }

}