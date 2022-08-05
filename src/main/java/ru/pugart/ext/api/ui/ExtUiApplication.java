package ru.pugart.ext.api.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class ExtUiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExtUiApplication.class, args);
	}
}
