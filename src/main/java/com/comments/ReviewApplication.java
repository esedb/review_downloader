package com.comments;


import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.comments.service.CustomKeyStore;

@SpringBootApplication
@EnableAsync
public class ReviewApplication {

	
	@Value("${developer_keys}")
	String developerkeys;
	
	@Value("${file_path}")
	String file_path;
	
	
	@PostConstruct
	void setKeys() {
		CustomKeyStore.setDeveloperkeys(developerkeys);
		UtilityService.filepath = file_path;
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ReviewApplication.class, args);
	
	}

}
