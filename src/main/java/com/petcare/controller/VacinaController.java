package com.petcare.controller;

import com.petcare.model.Pet;
import com.petcare.model.Usuario;
import com.petcare.model.Vacina;
import com.petcare.repository.PetRepository;
import com.petcare.repository.UsuarioRepository;
import com.petcare.repository.VacinaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal; // <-- IMPORT NECESSÁRIO
import java.time.LocalDate;
import java.util.List;

@Controller
public class VacinaController {

    private final VacinaRepository vacinaRepository;
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    public VacinaController(VacinaRepository vacinaRepository,
                            PetRepository petRepository,
                            UsuarioRepository usuarioRepository) {
        this.vacinaRepository = vacinaRepository;
        this.petRepository = petRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/vacinas")
    public String listarVacinas(Principal principal, Model model) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Pet> pets = petRepository.findByDono(usuario);

        model.addAttribute("pets", pets);
        model.addAttribute("usuario", usuario);

        return "vacinas"; // Página vacinas.html
    }

    @PostMapping("/vacinas/registrar")
    public String registrarVacina(Principal principal,
                                  @RequestParam Long petId,
                                  @RequestParam String nomeVacina,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAplicacao,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate proximaDose) {

        try {
            Usuario usuario = usuarioRepository.findByEmail(principal.getName());
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet não encontrado"));

            if (usuario == null || !pet.getDono().getId().equals(usuario.getId())) {
                return "redirect:/vacinas?error=Este pet não pertence a você.";
            }

            Vacina novaVacina = new Vacina();
            novaVacina.setNome(nomeVacina);
            novaVacina.setDataAplicacao(dataAplicacao);
            novaVacina.setProximaDose(proximaDose); // Será null se não for enviado
            novaVacina.setPet(pet);

            vacinaRepository.save(novaVacina);

            return "redirect:/vacinas?success=Vacina registrada com sucesso!";

        } catch (Exception e) {
            return "redirect:/vacinas?error=Não foi possível registrar a vacina.";
        }
    }
}