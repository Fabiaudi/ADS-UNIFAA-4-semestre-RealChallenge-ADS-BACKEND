package com.unifaa.bookexam;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BookexamApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void checkPasswordHashes() {
		System.out.println("Iniciando teste de hashes BCrypt...");
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = "RealChallengeUNIFAA";

		// Este é o hash que eu te passei no V6 (e que está no Supabase)
		String v6_BadHash = "$2a$10$4yY.F.3.T8lZcR.k8s0Uq.Y.mJ0Gf.WNT1dYxAsAIsC6S1XAXuIWS";
		
		// Este é o hash que eu estou propondo para o V7
		String v7_CorrectHash = "$2a$10$f6.wD7pEAX4s0dnHIy0Eie.fG9.S5N1H.r8XFrE4N.MvP1iKGlYfG";

		// Verificando
		boolean v6_matches = encoder.matches(password, v6_BadHash);
		boolean v7_matches = encoder.matches(password, v7_CorrectHash);

		System.out.println("=======================================================");
		System.out.println("Senha: " + password);
		System.out.println("Hash V6 (Errado): " + v6_BadHash);
		System.out.println("Hash V7 (Correto): " + v7_CorrectHash);
		System.out.println("---");
		System.out.println("Verificando V6 (Errado): " + v6_matches);
		System.out.println("Verificando V7 (Correto): " + v7_matches);
		System.out.println("=======================================================");
	}

}