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
<<<<<<<< HEAD:src/main/java/com/incresol/app/IncresolApplication.java
		SpringApplication.run(IncresolApplication.class, args);
========
		SpringApplication.run(SpringJwtAppApplication.class, args);
		 System.out.println("Hello App");
			 System.out.println("Hello App");
>>>>>>>> d4352a62435660cbfa165f4bddcb64e23b913196:src/main/java/com/incresol/app/SpringJwtAppApplication.java
	}

}
