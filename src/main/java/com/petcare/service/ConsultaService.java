package com.petcare.service;

import com.petcare.model.Consulta;
import com.petcare.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void salvarConsulta(Consulta consulta) {
        consultaRepository.save(consulta);
    }
}