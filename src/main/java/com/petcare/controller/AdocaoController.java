package com.petcare.controller;

import com.petcare.model.PetAdocao;
import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import com.petcare.service.AdocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal; // <-- IMPORT ADICIONADO

@Controller
@RequiredArgsConstructor
public class AdocaoController {

    private final AdocaoService adocaoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/adocao")
    public String listarPetsParaAdocao(Principal principal, Model model) { // <-- MUDANÇA
        String email = principal.getName(); // <-- MUDANÇA
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("pets", adocaoService.listarPetsDisponiveis());
        return "adocao";
    }

    @GetMapping("/adocao/pet/{id}")
    public String verPetAdocao(@PathVariable Long id, Principal principal, Model model) { // <-- MUDANÇA
        String email = principal.getName(); // <-- MUDANÇA
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) return "redirect:/login";

        try {
            model.addAttribute("usuario", usuario);
            model.addAttribute("pet", adocaoService.buscarPorId(id));
            return "adocao-detalhe";
        } catch (RuntimeException e) {
            return "redirect:/adocao?error=Pet não encontrado"; // <-- Email removido
        }
    }

    @GetMapping("/adocao/novo")
    public String formularioNovoPet(Principal principal, Model model) { // <-- MUDANÇA
        String email = principal.getName(); // <-- MUDANÇA
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "adocao-novo";
    }

    @PostMapping("/adocao/salvar")
    public String salvarPetAdocao(Principal principal, // <-- MUDANÇA
                                  @RequestParam String nome,
                                  @RequestParam String especie,
                                  @RequestParam String localizacao,
                                  @RequestParam String historia,
                                  @RequestParam String fotoUrl,
                                  @RequestParam(required = false) String raca,
                                  @RequestParam(required = false) String idade,
                                  @RequestParam(required = false) String sexo,
                                  @RequestParam(required = false) String porte) {

        String email = principal.getName(); // <-- MUDANÇA
        Usuario contato = usuarioRepository.findByEmail(email);
        if (contato == null) return "redirect:/login";

        PetAdocao pet = PetAdocao.builder()
                .nome(nome)
                .especie(especie)
                .localizacao(localizacao)
                .historia(historia)
                .fotoUrl(fotoUrl)
                .raca(raca)
                .idade(idade)
                .sexo(sexo)
                .porte(porte)
                .contato(contato)
                .build();

        adocaoService.cadastrarPet(pet);

        return "redirect:/adocao?success=Pet anunciado com sucesso!"; // <-- Email removido
    }

    @GetMapping("/adocao/pet/deletar/{id}")
    public String deletarPetAdocao(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) return "redirect:/login";

        try {
            PetAdocao pet = adocaoService.buscarPorId(id);


            if (pet.getContato().getId().equals(usuario.getId())) {
                adocaoService.removerPet(id);
                return "redirect:/adocao?success=Anúncio removido com sucesso";
            } else {
                return "redirect:/adocao?error=Permissão negada";
            }
        } catch (Exception e) {
            return "redirect:/adocao?error=Erro ao remover anúncio";
        }
    }
}