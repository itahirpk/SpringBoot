package com.spring.springjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringjwtApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



	public static void main(String[] args) {
		SpringApplication.run(SpringjwtApplication.class, args);

	}
}
