package com.johnsunday.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EntityScan(basePackages = { "com.johnsunday.app.entity", "com.johnsunday.app.security.entity",
		"com.johnsunday.app.security.controller" })
@EnableJpaRepositories(basePackages = { "com.johnsunday.app.dao", "com.johnsunday.app.security.dao" })
public class ExpenseNoteAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseNoteAppApplication.class, args);
	}

}
