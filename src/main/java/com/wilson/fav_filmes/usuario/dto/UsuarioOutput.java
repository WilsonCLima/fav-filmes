package com.wilson.fav_filmes.usuario.dto;

import com.wilson.fav_filmes.usuario.Role;
import lombok.Setter;

import java.time.LocalDate;

public record UsuarioOutput(
        Long id,
        String email,
        Role role,
        LocalDate dataCriacao
) {
}
