package com.miniyus.friday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FridayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridayApplication.class, args);
	}

}
