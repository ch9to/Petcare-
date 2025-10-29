package com.petcare.repository;

import com.petcare.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Método para buscar pets com vacinas
    @Query("SELECT DISTINCT p FROM Pet p LEFT JOIN FETCH p.vacinas WHERE p.dono.id = :usuarioId")
    List<Pet> findByDonoWithVacinas(@Param("usuarioId") Long usuarioId);

    // Método alternativo se o anterior não funcionar
    @Query("SELECT p FROM Pet p WHERE p.dono.id = :usuarioId")
    List<Pet> findByDono_Id(@Param("usuarioId") Long usuarioId);
}