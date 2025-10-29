package com.unifaa.bookexam.service;

import com.unifaa.bookexam.domain.users.User;
import com.unifaa.bookexam.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String role = u.getType() != null ? u.getType() : "STUDENT";
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles(role)
                .build();
    }
}