package com.wilson.fav_filmes.filme;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@RestController @Validated
@RequiredArgsConstructor
@RequestMapping("/api/filmes")
public class FilmeController {

    private final TmdbService tmdb;

    @GetMapping("/populares")
    public TmdbPagina populares(@RequestParam(defaultValue="1") @Min(1) int pagina) {
        return tmdb.populares(pagina);
    }

    @GetMapping("/buscar")
    public TmdbPagina buscar(@RequestParam @NotBlank String consulta, @RequestParam(defaultValue="1") @Min(1) int pagina) {
        return tmdb.buscar(consulta, pagina);
    }

    @GetMapping("/{id}")
    public TmdbFilme detalhes(@PathVariable @Positive Long id) {
        return tmdb.detalhar(id);
    }
}
