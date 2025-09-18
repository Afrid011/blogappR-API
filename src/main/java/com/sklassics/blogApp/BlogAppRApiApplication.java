package com.sklassics.blogApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.sklassics.blogApp.entity")
public class BlogAppRApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAppRApiApplication.class, args);
	}
}