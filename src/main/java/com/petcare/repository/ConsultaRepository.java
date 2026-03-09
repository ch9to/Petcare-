package com.petcare.repository;

import com.petcare.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT c FROM Consulta c WHERE c.usuario.id = :usuarioId AND c.dataHora > CURRENT_TIMESTAMP ORDER BY c.dataHora ASC")
    List<Consulta> findProximasConsultasByUsuario(Long usuarioId);
}
