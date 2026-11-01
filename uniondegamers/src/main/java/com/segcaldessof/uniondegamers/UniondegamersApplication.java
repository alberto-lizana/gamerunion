package com.segcaldessof.uniondegamers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan 
@SpringBootApplication
public class UniondegamersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniondegamersApplication.class, args);
	}

}
