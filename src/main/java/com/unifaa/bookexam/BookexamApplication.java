package com.unifaa.bookexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot (BookExam).
 * Ponto de entrada para o backend.
 */
@SpringBootApplication
public class BookexamApplication {

	/**
	 * Método main que inicia a aplicação Spring.
	 * @param args Argumentos de linha de comando (não utilizados).
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookexamApplication.class, args);
	}
}