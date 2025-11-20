package com.unifaa.bookexam.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unifaa.bookexam.model.entity.Subject;
import com.unifaa.bookexam.repository.SubjectQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectQueryRepository subjectRepository;
    
    public Optional<Subject> findById(UUID id) {
        return subjectRepository.findById(id);
    }
}
