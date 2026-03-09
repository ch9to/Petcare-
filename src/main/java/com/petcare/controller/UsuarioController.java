package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder; // <-- ADICIONADO PARA SEGURANÇA

    public UsuarioController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/meus-dados")
    public String meusDados(Principal principal, Model model) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        model.addAttribute("usuario", usuario);
        return "meus-dados";
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarDados(
            Principal principal, // <-- MUDANÇA 2
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam String endereco
    ) {
        String email = principal.getName(); // <-- MUDANÇA 3
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null) {
            usuario.setNome(nome);
            usuario.setTelefone(telefone);
            usuario.setEndereco(endereco);
            usuarioRepository.save(usuario);
        }

        return "redirect:/meus-dados";
    }

    @PostMapping("/usuario/alterar-senha")
    public String alterarSenha(
            Principal principal, // <-- MUDANÇA 2
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha
    ) {
        String email = principal.getName(); // <-- MUDANÇA 3
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && passwordEncoder.matches(senhaAtual, usuario.getSenha())) {

            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario);

            return "redirect:/meus-dados?success=Senha alterada com sucesso";
        }

        return "redirect:/meus-dados?error=Senha atual incorreta";
    }
}