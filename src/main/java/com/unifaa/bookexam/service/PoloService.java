package com.unifaa.bookexam.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.model.entity.Polo;
import com.unifaa.bookexam.repository.PoloRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PoloService {

    private final PoloRepository poloRepository;

    @Transactional(readOnly = true)
    public int getCapacity(String poloId) {
        Optional<Polo> poloOpt = poloRepository.findById(poloId);
        return poloOpt.map(Polo::getAvailability).orElse(0);
    }

    @Transactional(readOnly = true)
    public Optional<Polo> findById(String poloId) {
        return poloRepository.findById(poloId);
    }
}
