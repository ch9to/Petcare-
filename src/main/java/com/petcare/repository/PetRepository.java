package com.petcare.repository;

import com.petcare.model.Pet;
import com.petcare.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT DISTINCT p FROM Pet p LEFT JOIN FETCH p.vacinas WHERE p.dono.id = :usuarioId")
    List<Pet> findByDonoWithVacinas(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Pet p WHERE p.dono.id = :usuarioId")
    List<Pet> findByDono_Id(@Param("usuarioId") Long usuarioId);

    List<Pet> findByDono(Usuario usuario);
}