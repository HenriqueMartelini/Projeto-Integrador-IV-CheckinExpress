package com.checkinExpress.checkin_express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CheckinExpressApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure().load();

		// Recupera as variáveis de ambiente
		String mongodbUrl = dotenv.get("DATABASE_URL");
		String mongodbUser = dotenv.get("DATABASE_USER");
		String mongodbPassword = dotenv.get("DATABASE_PASSWORD");

		// Verifica se as variáveis estão carregadas corretamente
		if (mongodbUrl == null || mongodbUser == null || mongodbPassword == null) {
			throw new IllegalStateException("As variáveis de ambiente não foram encontradas no arquivo .env.");
		}

		// Configura as variáveis como variáveis de sistema (se necessário)
		System.setProperty("DATABASE_URL", mongodbUrl);
		System.setProperty("DATABASE_USER", mongodbUser);
		System.setProperty("DATABASE_PASSWORD", mongodbPassword);

		SpringApplication.run(CheckinExpressApplication.class, args);
	}
}