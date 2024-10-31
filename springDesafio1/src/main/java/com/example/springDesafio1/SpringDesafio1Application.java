package com.example.springDesafio1;

import com.example.springDesafio1.Reposity.LibrosRepository;
import com.example.springDesafio1.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDesafio1Application implements CommandLineRunner {

	@Autowired
	private LibrosRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(SpringDesafio1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.muestraElMenu();

	}



}

