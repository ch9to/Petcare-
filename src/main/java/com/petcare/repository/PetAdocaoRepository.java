package com.petcare.repository;

import com.petcare.model.PetAdocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetAdocaoRepository extends JpaRepository<PetAdocao, Long> {

    List<PetAdocao> findByStatusOrderByDataPublicacaoDesc(String status);
}