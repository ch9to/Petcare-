package com.petcare.controller;

import com.petcare.model.*;
import com.petcare.repository.*;
import com.petcare.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final ConsultaService consultaService;

    @GetMapping("/")
    public String redirecionarParaLogin() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String exibirHome(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        model.addAttribute("usuario", usuario);
        model.addAttribute("mensagem", "Bem-vindo, " + usuario.getNome() + "!");
        return "home";
    }

    @GetMapping("/agendamentos")
    public String exibirAgendamentos(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        List<Pet> pets = petRepository.findByDono_Id(usuario.getId());

        model.addAttribute("pets", pets);
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("usuario", usuario);
        return "agendamento";
    }

    @PostMapping("/consultas/agendar")
    public String agendarConsulta(
            @RequestParam String email,
            @RequestParam Long petId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            @RequestParam String descricao
    ) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email);
            Pet pet = petRepository.findById(petId).orElseThrow();

            Consulta consulta = new Consulta();
            consulta.setDataHora(dataHora);
            consulta.setDescricao(descricao);
            consulta.setPet(pet);
            consulta.setUsuario(usuario);

            consultaService.salvarConsulta(consulta);
            return "redirect:/home?email=" + email + "&success=Consulta agendada com sucesso";

        } catch (Exception e) {
            return "redirect:/agendamentos?email=" + email + "&error=Erro ao agendar consulta";
        }
    }
}