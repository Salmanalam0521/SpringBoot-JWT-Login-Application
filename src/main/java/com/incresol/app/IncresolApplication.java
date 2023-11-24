package com.incresol.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Profile("!"+IncresolApplication.DEV_PROFILE)
public class IncresolApplication {
	public static final String DEV_PROFILE = "dev";
	public static void main(String[] args) {
		SpringApplication.run(IncresolApplication.class, args);
	}

}
