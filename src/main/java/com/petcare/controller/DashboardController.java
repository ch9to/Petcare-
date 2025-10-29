package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String exibirDashboard(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}