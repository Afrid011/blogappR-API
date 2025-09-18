package com.sklassics.blogApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan("com.sklassics.blogApp.entity")
public class BlogAppRApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlogAppRApiApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOrigins(
            "http://localhost:3000",
            "https://blogappr-api-1.onrender.com"
          );
      }
    };
  }
}