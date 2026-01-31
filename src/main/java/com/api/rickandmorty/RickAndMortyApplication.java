package com.api.rickandmorty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RickAndMortyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RickAndMortyApplication.class, args);
	}

}
