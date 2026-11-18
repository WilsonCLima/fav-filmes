package com.wilson.fav_filmes.favorito;
import java.time.*;
public record FavoritoOutput(Long id, String titulo, String sinopse,
                             LocalDate dataLancamento, String posterPath,
                             LocalDateTime favoritedAt) {
    static FavoritoOutput of(Favorito favorito) {
        var filme = favorito.getFilme();
        return new FavoritoOutput(filme.getId(), filme.getTitulo(), filme.getSinopse(),
                                  filme.getDataLancamento(), filme.getPosterPath(),
                                  favorito.getFavoritedAt());
    }
}
