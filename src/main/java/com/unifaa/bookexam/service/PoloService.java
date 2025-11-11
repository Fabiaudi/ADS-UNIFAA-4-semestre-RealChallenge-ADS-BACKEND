package com.unifaa.bookexam.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifaa.bookexam.model.entity.Polo;
import com.unifaa.bookexam.repository.PoloRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PoloService {

    private final PoloRepository poloRepository;

    /**
     * Retorna o polo pelo ID
     *
     * @param poloId - matrícula do polo (ex: "P00001")
     * @return Polo ou null se não existir
     */
    @Transactional(readOnly = true)
    public Polo getPoloById(UUID poloId) {
        return poloRepository.findById(poloId).orElse(null);
    }

    /**
     * Retorna a capacidade disponível do polo
     *
     * @param poloId - matrícula do polo
     * @return capacidade (availability) ou 0 se o polo não existir
     */
    @Transactional(readOnly = true)
    public int getAvailability(UUID poloId) {
        Optional<Polo> poloOpt = poloRepository.findById(poloId);
        return poloOpt.map(Polo::getAvailability).orElse(0);
    }

    @Transactional(readOnly = true)
    public int getCapacity(UUID poloId) {
        Optional<Polo> poloOpt = poloRepository.findById(poloId);
        return poloOpt.map(Polo::getAvailability).orElse(0);
    }

    @Transactional(readOnly = true)
    public Optional<Polo> findById(UUID poloId) {
        return poloRepository.findById(poloId);
    }
}
