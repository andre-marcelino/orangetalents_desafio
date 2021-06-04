package com.orangetalents.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrangetalentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrangetalentsApplication.class, args);
	}

}
