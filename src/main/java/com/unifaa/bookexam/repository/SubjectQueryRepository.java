package com.unifaa.bookexam.repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.unifaa.bookexam.model.entity.Subject;

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

     /** Retorna o Subject completo pelo ID. */
    public Optional<Subject> findById(UUID id) {
        String sql = "SELECT id, name FROM subjects WHERE id = :id";
        
        try {
            Subject subject = jdbc.queryForObject(sql, 
                new MapSqlParameterSource("id", id),
                (rs, rowNum) -> {
                    Subject sub = new Subject();
                    sub.setId(rs.getObject("id", UUID.class));
                    sub.setName(rs.getString("name"));
                    return sub;
                });
            return Optional.ofNullable(subject);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}