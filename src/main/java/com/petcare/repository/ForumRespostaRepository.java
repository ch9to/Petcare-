package com.petcare.repository;

import com.petcare.model.ForumResposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRespostaRepository extends JpaRepository<ForumResposta, Long> {
}
