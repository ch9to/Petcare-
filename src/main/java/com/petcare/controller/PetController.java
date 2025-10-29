package com.petcare.controller;

import com.petcare.model.*;
import com.petcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PetController {
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/pets")
    public String listarPets(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        model.addAttribute("pets", petRepository.findByDono_Id(usuario.getId()));
        model.addAttribute("usuario", usuario);
        return "pets";
    }

    @PostMapping("/pets/cadastrar")
    public String cadastrarPet(
            @RequestParam String email,
            @RequestParam String nome,
            @RequestParam String especie,
            @RequestParam(required = false) String raca,
            @RequestParam(required = false) Integer idade
    ) {
        Usuario dono = usuarioRepository.findByEmail(email);

        Pet pet = Pet.builder()
                .nome(nome)
                .especie(especie)
                .raca(raca)
                .idade(idade)
                .dono(dono)
                .build();

        petRepository.save(pet);
        return "redirect:/pets?email=" + email;
    }
}