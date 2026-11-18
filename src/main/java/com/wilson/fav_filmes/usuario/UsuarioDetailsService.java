package com.wilson.fav_filmes.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Carrega o usuário pelo e-mail para a autenticação por sessão. */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }
}
