package com.wilson.fav_filmes.usuario;

import com.wilson.fav_filmes.usuario.dto.*;
import com.wilson.fav_filmes.infra.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioAssembler assembler;
    private final PasswordEncoder encoder;

    @Transactional
    public UsuarioOutput cadastrar(UsuarioInput input) {
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmailIgnoreCase(email))
            throw new ApiException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        Usuario usuario = assembler.dtoToEntity(input);
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode(input.senha()));
        usuario.setRole(Role.USER);
        return assembler.entityToDto(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public Usuario buscarAutenticado(String email) {
        return repository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public UsuarioOutput perfil(String email) {
        return assembler.entityToDto(buscarAutenticado(email));
    }
}
