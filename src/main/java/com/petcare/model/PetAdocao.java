package com.petcare.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especie; // Ex: Cão, Gato

    private String raca;
    private String idade; // Ex: "Aprox. 2 anos", "Filhote"
    private String sexo; // Ex: Macho, Fêmea
    private String porte; // Ex: Pequeno, Médio, Grande

    @Column(nullable = false)
    private String localizacao; // Ex: "São Paulo, SP"

    @Lob
    @Column(columnDefinition = "TEXT")
    private String historia;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fotoUrl; // URL de uma imagem do pet

    @Column(nullable = false)
    private String status; // "Disponível", "Adotado"

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataPublicacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_contato_id", nullable = false)
    private Usuario contato; // Usuário que publicou

    @PrePersist
    protected void onCreate() {
        this.dataPublicacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Disponível";
        }
    }
}