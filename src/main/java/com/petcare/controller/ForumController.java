package com.petcare.controller;

import com.petcare.model.ForumPost;
import com.petcare.model.Usuario;
import com.petcare.repository.UsuarioRepository;
import com.petcare.service.ForumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class ForumController {

    private final ForumService forumService;
    private final UsuarioRepository usuarioRepository;

    public ForumController(ForumService forumService, UsuarioRepository usuarioRepository) {
        this.forumService = forumService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/forum")
    public String listarForum(Principal principal, Model model) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("posts", forumService.listarTodosPosts());
        return "forum"; //
    }

    @GetMapping("/forum/post/{id}")
    public String verPost(@PathVariable Long id, Principal principal, Model model) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            ForumPost post = forumService.buscarPostPorId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("post", post);
            return "forum-post"; //
        } catch (RuntimeException e) {
            return "redirect:/forum?error=Post não encontrado";
        }
    }

    @PostMapping("/forum/post/criar")
    public String criarPost(Principal principal,
                            @RequestParam String titulo,
                            @RequestParam String conteudo) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        forumService.criarNovoPost(titulo, conteudo, usuario);

        return "redirect:/forum";
    }

    @PostMapping("/forum/post/{postId}/responder")
    public String criarResposta(@PathVariable Long postId,
                                Principal principal,
                                @RequestParam String conteudo) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            ForumPost post = forumService.buscarPostPorId(postId);
            forumService.adicionarResposta(conteudo, usuario, post);

            return "redirect:/forum/post/" + postId;
        } catch (RuntimeException e) {
            return "redirect:/forum?error=Não foi possível responder";
        }
    }

    @GetMapping("/forum/post/deletar/{id}")
    public String deletarPost(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            ForumPost post = forumService.buscarPostPorId(id);

            if (post.getAutor().getId().equals(usuario.getId())) {
                forumService.deletarPost(id); // Este método foi criado no passo anterior no Service
                return "redirect:/forum?success=Post removido com sucesso";
            } else {
                return "redirect:/forum?error=Você não tem permissão para remover este post";
            }
        } catch (Exception e) {
            return "redirect:/forum?error=Erro ao remover post";
        }
    }
}