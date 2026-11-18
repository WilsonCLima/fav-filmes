package com.wilson.fav_filmes.usuario;

import com.wilson.fav_filmes.usuario.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioOutput> cadastrar(@RequestBody @Valid UsuarioInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(input));
    }

    @GetMapping("/me")
    public UsuarioOutput meuPerfil(Authentication authentication) {
        return service.perfil(authentication.getName());
    }
}
