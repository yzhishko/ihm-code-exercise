package com.code.api.advertise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AdvertiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertiseApplication.class, args);
	}
}
