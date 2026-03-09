package com.petcare.service;

import com.petcare.model.PetAdocao;
import com.petcare.repository.PetAdocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdocaoService {

    private final PetAdocaoRepository petAdocaoRepository;

    public List<PetAdocao> listarPetsDisponiveis() {
        return petAdocaoRepository.findByStatusOrderByDataPublicacaoDesc("Disponível");
    }

    public PetAdocao buscarPorId(Long id) {
        return petAdocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet para adoção não encontrado"));
    }

    public void cadastrarPet(PetAdocao pet) {
        // A lógica de pre-persist no Model já define data e status
        petAdocaoRepository.save(pet);
    }

    public void removerPet(Long id) {
        petAdocaoRepository.deleteById(id);
    }
}
