package com.unifaa.bookexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifaa.bookexam.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
