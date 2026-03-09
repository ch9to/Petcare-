package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal; // <-- IMPORT NECESSÁRIO

@Controller
public class DashboardController {

    private final UsuarioRepository usuarioRepository;

    public DashboardController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/dashboard")
    public String exibirDashboard(Principal principal, Model model) {

        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}