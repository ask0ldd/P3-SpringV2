package com.example.immo;

import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Role;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.services.MessageService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ImmoApplication implements CommandLineRunner { // !!! commandlinerunner added soutenance

	private final RoleRepository roleRepository;

	public ImmoApplication(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ImmoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// init role table !!!! try catch
		if (roleRepository.findByAuthority("ADMIN").isPresent())
			return;
		roleRepository.save(new Role("ADMIN"));
		roleRepository.save(new Role("USER"));
	}

}

// System.out.println("\u001B[31m" + username + "\u001B[0m");
