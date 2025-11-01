package com.unifaa.bookexam.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserQueryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public UserQueryRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Retorna Optional<String> com o type do user (ADMIN|POLO|STUDENT) ou empty se não existir. */
    public Optional<String> findTypeById(String userId) {
        String sql = "SELECT type FROM users WHERE id = :id";
        var list = jdbc.query(sql, Map.of("id", userId), (rs, n) -> rs.getString("type"));
        return list.stream().findFirst();
    }
}