package com.unifaa.bookexam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifaa.bookexam.model.entity.User;

/**
 * Repository da entidade User.
 */
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
