package com.checkinExpress.checkin_express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CheckinExpressApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure().load();

		String mongoUri = dotenv.get("MONGODB_URI");
		String mongoDatabase = dotenv.get("MONGODB_DATABASE");

		if (mongoUri == null || mongoDatabase == null) {
			throw new IllegalStateException("As variáveis de ambiente MONGODB_URI e MONGODB_DATABASE são obrigatórias.");
		}

		// Configura as variáveis como variáveis de sistema
		System.setProperty("MONGODB_URI", mongoUri);
		System.setProperty("MONGODB_DATABASE", mongoDatabase);

		SpringApplication.run(CheckinExpressApplication.class, args);
	}
}