package com.self.taskintervale.demoREST;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//Для swagger-а описание при генерации
@OpenAPIDefinition(info = @Info(title = "Функционал REST приложения", version = "1.0.0",
		description = "Тут отображены запросы, которые может принимать контроллер"),
		servers = @Server(url = "http://localhost:8080", description = "URL Сервера"))
public class DemoRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestApplication.class, args);
	}

}
