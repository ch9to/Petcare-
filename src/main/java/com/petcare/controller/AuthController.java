package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String paginaLogin(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              Model model) {

        if (error != null) {
            model.addAttribute("error", "E-mail ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("logout", "Você foi desconectado com sucesso.");
        }

        model.addAttribute("usuario", new Usuario());
        return "login"; // Mostra a página login.html
    }

    @GetMapping("/registro")
    public String paginaRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Mostra a página registro.html
    }

    @PostMapping("/registro/salvar")
    public String registrarUsuario(Usuario usuario, Model model) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            model.addAttribute("error", "Este e-mail já está cadastrado.");
            model.addAttribute("usuario", usuario);
            return "registro";
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        return "redirect:/login?success=Registro concluído com sucesso";
    }
}