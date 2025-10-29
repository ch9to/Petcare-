package com.petcare.controller;

import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/meus-dados")
    public String meusDados(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        model.addAttribute("usuario", usuario);
        return "meus-dados";
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarDados(
            @RequestParam String email,
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam String endereco
    ) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setEndereco(endereco);
        usuarioRepository.save(usuario);
        return "redirect:/meus-dados?email=" + email;
    }

    @PostMapping("/usuario/alterar-senha")
    public String alterarSenha(
            @RequestParam String email,
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha
    ) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario.getSenha().equals(senhaAtual)) {
            usuario.setSenha(novaSenha);
            usuarioRepository.save(usuario);
            return "redirect:/meus-dados?email=" + email + "&success=Senha alterada com sucesso";
        }
        return "redirect:/meus-dados?email=" + email + "&error=Senha atual incorreta";
    }
}