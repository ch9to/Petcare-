package com.petcare.controller;

import com.petcare.model.*;
import com.petcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VacinaController {
    private final VacinaRepository vacinaRepository;
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/vacinas")
    public String listarVacinas(@RequestParam String email, Model model) {
        // Verifica se o usuário existe
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return "redirect:/login";
        }

        // Carrega os pets com suas vacinas
        List<Pet> pets = petRepository.findByDono_Id(usuario.getId());

        // Debug: Verifique no console se os dados estão sendo carregados
        System.out.println("Pets encontrados: " + pets.size());
        pets.forEach(pet -> {
            System.out.println("Pet: " + pet.getNome() + " - Vacinas: " +
                    (pet.getVacinas() != null ? pet.getVacinas().size() : 0));
        });

        model.addAttribute("pets", pets);
        model.addAttribute("usuario", usuario);
        return "vacinas";
    }

    @PostMapping("/vacinas/registrar")
    public String registrarVacina(
            @RequestParam String email,
            @RequestParam Long petId,
            @RequestParam String nomeVacina,
            @RequestParam LocalDate dataAplicacao,
            @RequestParam(required = false) LocalDate proximaDose
    ) {
        Pet pet = petRepository.findById(petId).orElseThrow();

        Vacina vacina = Vacina.builder()
                .nome(nomeVacina)
                .dataAplicacao(dataAplicacao)
                .proximaDose(proximaDose)
                .completa(proximaDose == null)
                .pet(pet)
                .build();

        vacinaRepository.save(vacina);
        return "redirect:/vacinas?email=" + email;
    }
}