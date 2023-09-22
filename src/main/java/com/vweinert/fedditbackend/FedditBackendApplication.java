package com.vweinert.fedditbackend;

import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vweinert.fedditbackend.repository.RoleRepository;

@SpringBootApplication
public class FedditBackendApplication {
	private static final Logger logger = LoggerFactory.getLogger(FedditBackendApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FedditBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner init (RoleRepository roleRepo) {
		return args -> {

		};
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
