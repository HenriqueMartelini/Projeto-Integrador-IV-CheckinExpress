package com.checkinExpress.checkin_express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CheckinExpressApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure().load();

		// Configura as variáveis como variáveis de sistema
		System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		System.setProperty("MONGODB_DATABASE", dotenv.get("MONGODB_DATABASE"));

		SpringApplication.run(CheckinExpressApplication.class, args);
	}
}