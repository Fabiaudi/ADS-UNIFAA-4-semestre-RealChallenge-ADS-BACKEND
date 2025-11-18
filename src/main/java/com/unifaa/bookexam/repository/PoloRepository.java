package com.unifaa.bookexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unifaa.bookexam.model.entity.Polo;

@Repository
public interface PoloRepository extends JpaRepository<Polo, String> {

}
