package com.jwdnd.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class []{ SecurityConfig.class, CloudStorageApplication.class}, args);
	}    
}
