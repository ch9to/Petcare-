package com.petcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario dono;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Vacina> vacinas;
}
