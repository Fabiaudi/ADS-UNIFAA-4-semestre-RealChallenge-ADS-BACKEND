package com.unifaa.bookexam.repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Consulta leve à tabela 'subjects' sem precisar mapear a entidade JPA.
 * Útil apenas para validação de existência por id (UUID).
 */
@Repository
public class SubjectQueryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public SubjectQueryRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Retorna true se existir subject com o id informado. */
    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM subjects WHERE id = :id LIMIT 1";
        var list = jdbc.query(sql, Map.of("id", id), (rs, n) -> 1);
        return !list.isEmpty();
    }

    /** Opcional: retorna o nome do subject, se existir. */
    public Optional<String> findNameById(UUID id) {
        String sql = "SELECT name FROM subjects WHERE id = :id";
        var list = jdbc.query(sql, Map.of("id", id), (rs, n) -> rs.getString("name"));
        return list.stream().findFirst();
    }
}
