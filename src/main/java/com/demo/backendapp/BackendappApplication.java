package com.demo.backendapp;

import com.demo.backendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendappApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackendappApplication.class, args);
	}

	@Autowired
	UserService userService;

	@Override
	public void run(String... args) throws Exception {
		userService.createAdminUser();
	}
}
