package com.petcare.service;

import com.petcare.model.Pet;
import com.petcare.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findByDonoId(Long usuarioId) {
        return petRepository.findByDono_Id(usuarioId);
    }
}
