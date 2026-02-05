package com.adaptive.lms.adaptive_lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Remove the (exclude = ...) part
public class AdaptiveLmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdaptiveLmsApplication.class, args);
	}
}