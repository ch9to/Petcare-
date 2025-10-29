package com.petcare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String sexo;
    private String cidade;
    private String estado;
    private String endereco;
}