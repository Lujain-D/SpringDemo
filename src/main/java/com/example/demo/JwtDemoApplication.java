package com.example.demo;

import com.example.demo.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class JwtDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtDemoApplication.class, args);
	}

//	@RequestMapping("/hello")
//	public String hello(){
//		return "Hello World!";
//	}
}
