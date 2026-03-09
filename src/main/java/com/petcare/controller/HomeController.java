package com.petcare.controller;

import com.petcare.model.*;
import com.petcare.repository.*;
import com.petcare.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // <-- IMPORT ADICIONADO
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final ConsultaService consultaService;
    private final ConsultaRepository consultaRepository;


    @GetMapping("/home")
    public String exibirHome(Principal principal, Model model) { // <-- MUDANÇA AQUI
        String email = principal.getName(); // <-- MUDANÇA AQUI
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Pet> pets = petRepository.findByDonoWithVacinas(usuario.getId());

        long vacinasEmDia = pets.stream()
                .flatMap(pet -> pet.getVacinas().stream())
                .filter(Vacina::isCompleta)
                .count();

        List<Consulta> consultas = consultaRepository.findProximasConsultasByUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("mensagem", "Bem-vindo, " + usuario.getNome() + "!");
        model.addAttribute("totalPets", pets.size());
        model.addAttribute("totalConsultas", consultas.size());
        model.addAttribute("totalVacinasEmDia", vacinasEmDia);

        return "home";
    }

    @GetMapping("/agendamentos")
    public String exibirAgendamentos(Principal principal, Model model) { // <-- MUDANÇA AQUI
        String email = principal.getName(); // <-- MUDANÇA AQUI
        Usuario usuario = usuarioRepository.findByEmail(email);
        List<Pet> pets = petRepository.findByDono_Id(usuario.getId());

        model.addAttribute("pets", pets);
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("usuario", usuario);
        return "agendamento";
    }

    @PostMapping("/consultas/agendar")
    public String agendarConsulta(
            Principal principal, // <-- MUDANÇA AQUI
            @RequestParam Long petId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            @RequestParam String descricao
    ) {
        try {
            String email = principal.getName(); // <-- MUDANÇA AQUI
            Usuario usuario = usuarioRepository.findByEmail(email);
            Pet pet = petRepository.findById(petId).orElseThrow();

            Consulta consulta = new Consulta();
            consulta.setDataHora(dataHora);
            consulta.setDescricao(descricao);
            consulta.setPet(pet);
            consulta.setUsuario(usuario);

            consultaService.salvarConsulta(consulta);
            return "redirect:/home?success=Consulta agendada com sucesso"; // <-- Email removido

        } catch (Exception e) {
            return "redirect:/agendamentos?error=Erro ao agendar consulta"; // <-- Email removido
        }
    }
}