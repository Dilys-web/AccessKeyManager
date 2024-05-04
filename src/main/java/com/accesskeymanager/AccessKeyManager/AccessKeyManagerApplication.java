package com.accesskeymanager.AccessKeyManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication

@EnableCaching
public class AccessKeyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessKeyManagerApplication.class, args);
	}
}
