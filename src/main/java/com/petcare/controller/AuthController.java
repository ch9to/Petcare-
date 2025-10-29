package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(
            @RequestParam String email,
            @RequestParam String senha,
            Model model
    ) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            return "redirect:/home?email=" + email;
        }

        model.addAttribute("erro", "Email ou senha incorretos");
        return "login";
    }

    @GetMapping("/registro")
    public String exibirFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String processarRegistro(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/login?registroSucesso";
    }

}