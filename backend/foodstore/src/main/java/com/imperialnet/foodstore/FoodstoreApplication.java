package com.imperialnet.foodstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FoodstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodstoreApplication.class, args);
	}

}
