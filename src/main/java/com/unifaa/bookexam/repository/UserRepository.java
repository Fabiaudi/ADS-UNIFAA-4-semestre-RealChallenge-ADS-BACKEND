package com.unifaa.bookexam.repository;

import com.unifaa.bookexam.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
