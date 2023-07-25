package com.example.syncrowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SyncrowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyncrowBackendApplication.class, args);
	}

}
