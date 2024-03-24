package com.example.immo;

import com.example.immo.models.Role;
import com.example.immo.repositories.RoleRepository;
import com.example.immo.services.FileSystemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImmoApplication implements CommandLineRunner { // !!! commandlinerunner added soutenance

	private final RoleRepository roleRepository;
	private final FileSystemService fileSystemService;

	public ImmoApplication(RoleRepository roleRepository, FileSystemService fileSystemService) {
		this.roleRepository = roleRepository;
		this.fileSystemService = fileSystemService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ImmoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// emptying the folder containing all the uploaded pictures
		fileSystemService.deleteAllFiles();
		// init role table
		roleRepository.findByAuthority("ADMIN").ifPresentOrElse(a -> {}, () -> {
			roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));
		});
	}

}

// System.out.println("\u001B[31m" + username + "\u001B[0m");

// data sql
