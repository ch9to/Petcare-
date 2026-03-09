package com.petcare.controller;

import com.petcare.model.Pet;
import com.petcare.model.Usuario;
import com.petcare.repository.PetRepository;
import com.petcare.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class PetController {

    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

    public PetController(PetRepository petRepository, UsuarioRepository usuarioRepository) {
        this.petRepository = petRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/pets")
    public String verPets(Principal principal, Model model) {

        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Pet> pets = petRepository.findByDono(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("pets", pets);
        return "pets"; // Página pets.html
    }

    @PostMapping("/pets/salvar")
    public String salvarPet(Principal principal,
                            @RequestParam String nome,
                            @RequestParam String especie,
                            @RequestParam String raca,
                            @RequestParam int idade,
                            Model model) {

        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        Pet pet = new Pet();
        pet.setNome(nome);
        pet.setEspecie(especie);
        pet.setRaca(raca);
        pet.setIdade(idade);
        pet.setDono(usuario);

        petRepository.save(pet);


        return "redirect:/pets";
    }
}