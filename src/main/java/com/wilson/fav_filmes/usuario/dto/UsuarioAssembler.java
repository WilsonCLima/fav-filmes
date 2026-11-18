package com.wilson.fav_filmes.usuario.dto;

import com.wilson.fav_filmes.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    public Usuario dtoToEntity(UsuarioInput usuarioInput) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioInput.email());
        usuario.setSenha(usuarioInput.senha());
        return usuario;
    }

    public UsuarioOutput entityToDto(Usuario usuario) {
        return new UsuarioOutput(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getDataCriacao()
        );
    }
}
