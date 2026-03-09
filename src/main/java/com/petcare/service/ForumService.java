package com.petcare.service;

import com.petcare.model.ForumPost;
import com.petcare.model.ForumResposta;
import com.petcare.model.Usuario;
import com.petcare.repository.ForumPostRepository;
import com.petcare.repository.ForumRespostaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {

    private final ForumPostRepository forumPostRepository;
    private final ForumRespostaRepository forumRespostaRepository;

    public ForumService(ForumPostRepository forumPostRepository,
                        ForumRespostaRepository forumRespostaRepository) {
        this.forumPostRepository = forumPostRepository;
        this.forumRespostaRepository = forumRespostaRepository;
    }

    public List<ForumPost> listarTodosPosts() {
        return forumPostRepository.findAllByOrderByDataCriacaoDesc(); //
    }

    public ForumPost buscarPostPorId(Long id) {
        return forumPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado")); //
    }

    public void criarNovoPost(String titulo, String conteudo, Usuario autor) {
        ForumPost post = new ForumPost();
        post.setTitulo(titulo);
        post.setConteudo(conteudo);
        post.setAutor(autor);
        forumPostRepository.save(post); //
    }

    public void adicionarResposta(String conteudo, Usuario autor, ForumPost post) {
        ForumResposta resposta = new ForumResposta();
        resposta.setConteudo(conteudo);
        resposta.setAutor(autor);
        resposta.setPost(post);
        forumRespostaRepository.save(resposta); //
    }


    public void deletarPost(Long id) {
        forumPostRepository.deleteById(id);
    }
}