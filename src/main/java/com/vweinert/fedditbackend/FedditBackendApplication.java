package com.vweinert.fedditbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vweinert.fedditbackend.models.ERole;
import com.vweinert.fedditbackend.models.Role;
import com.vweinert.fedditbackend.repository.RoleRepository;

@SpringBootApplication
public class FedditBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FedditBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner init (RoleRepository roleRepo) {
		return args -> {
			for(ERole role: ERole.values()) {
				roleRepo.save(new Role(role));
			}
		};
	}
}
