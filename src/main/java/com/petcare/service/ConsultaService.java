package com.petcare.service;

import com.petcare.model.Consulta;
import com.petcare.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaService {
    private final ConsultaRepository consultaRepository;

    public void salvarConsulta(Consulta consulta) {
        consultaRepository.save(consulta);
    }
}