package com.unifaa.bookexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifaa.bookexam.model.entity.User;

import java.util.Optional;

/**
 * Interface JPA Repository para a entidade {@link User}.
 * Fornece métodos CRUD básicos e consultas customizadas para usuários.
 * A chave primária (ID) é do tipo String (matrícula).
 */
public interface UserRepository extends JpaRepository<User, String> {
	/**
     * Busca um usuário pelo seu endereço de email único.
     *
     * @param email O email a ser buscado.
     * @return Um {@link Optional} contendo o {@link User} se encontrado,
     * ou vazio se não encontrado.
     */
    Optional<User> findByEmail(String email);
}
