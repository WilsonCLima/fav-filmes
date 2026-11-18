package com.wilson.fav_filmes.favorito;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService service;

    @GetMapping
    public List<FavoritoOutput> listar(Authentication auth) {
        return service.listar(auth.getName());
    }

    @PostMapping("/{tmdbId}")
    public ResponseEntity<FavoritoOutput> adicionar(Authentication auth, @PathVariable Long tmdbId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionar(auth.getName(), tmdbId));
    }

    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> remover(Authentication auth, @PathVariable Long tmdbId) {
        service.remover(auth.getName(), tmdbId);
        return ResponseEntity.noContent().build();
    }
}
