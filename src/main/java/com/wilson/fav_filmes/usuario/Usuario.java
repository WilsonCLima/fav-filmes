package com.wilson.fav_filmes.usuario;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @PrePersist
    void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDate.now();
        if (role == null) role = Role.USER;
    }
}
