package com.wilson.fav_filmes.usuario.dto;
import jakarta.validation.constraints.*;

public record UsuarioInput(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min=8, max=72)
        String senha
)
{}
