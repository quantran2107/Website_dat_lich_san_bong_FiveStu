package com.example.DATN_WebFiveTus;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DatnWebFiveTusApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatnWebFiveTusApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}



}
