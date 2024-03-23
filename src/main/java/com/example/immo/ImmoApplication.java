package com.example.immo;

import com.example.immo.models.Role;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.services.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.BiFunction;
import java.util.function.Function;

@SpringBootApplication
public class ImmoApplication implements CommandLineRunner { // !!! commandlinerunner added soutenance

	private final RoleRepository roleRepository;
	private final FileService fileService;

	public ImmoApplication(RoleRepository roleRepository, FileService fileService) {
		this.roleRepository = roleRepository;
		this.fileService=fileService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ImmoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// emptying the folder containing all the uploaded pictures
		fileService.deleteAll();
		// init role table
		if (roleRepository.findByAuthority("ADMIN").isPresent()) return; // if empty
		roleRepository.save(new Role("ADMIN"));
		roleRepository.save(new Role("USER"));
		/*roleRepository.findByAuthority("ADMIN").orElseGet(() -> {
			roleRepository.save(new Role("ADMIN"));
			return roleRepository.save(new Role("USER"));
		}); // if em*/
		/*roleRepository.findByAuthority("ADMIN").ifPresentOrElse(a -> {}, () -> {
			roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));
		});*/

		// BiFunction
	}

}

// System.out.println("\u001B[31m" + username + "\u001B[0m");

// data sql
