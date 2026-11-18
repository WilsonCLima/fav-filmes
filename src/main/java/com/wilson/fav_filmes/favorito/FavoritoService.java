package com.wilson.fav_filmes.favorito;

import com.wilson.fav_filmes.filme.*;
import com.wilson.fav_filmes.usuario.*;
import com.wilson.fav_filmes.infra.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final UsuarioService usuarios;
    private final FilmeRepository filmes;
    private final FavoritoRepository favoritos;
    private final TmdbService tmdb;

    @Transactional(readOnly = true)
    public List<FavoritoOutput> listar(String email) {
        Long id = usuarios.buscarAutenticado(email).getId();
        return favoritos.findByUsuarioIdOrderByFavoritedAtDesc(id)
            .stream().map(FavoritoOutput::of).toList();
    }

    @Transactional
    public FavoritoOutput adicionar(String email, Long tmdbId) {
        var usuario = usuarios.buscarAutenticado(email);
        if (favoritos.existsByUsuarioIdAndFilmeId(usuario.getId(), tmdbId))
            throw new ApiException(HttpStatus.CONFLICT, "Filme já está nos favoritos");
        var filme = filmes.findById(tmdbId).orElseGet(() -> {
            TmdbFilme resultado = tmdb.detalhar(tmdbId);
            Filme novo = new Filme();
            novo.setId(resultado.id());
            novo.setTitulo(resultado.title());
            novo.setSinopse(resultado.overview());
            novo.setDataLancamento(resultado.releaseDate());
            novo.setPosterPath(resultado.posterPath());
            return filmes.save(novo);
        });
        return FavoritoOutput.of(favoritos.save(new Favorito(usuario, filme)));
    }

    @Transactional
    public void remover(String email, Long tmdbId) {
        var usuario = usuarios.buscarAutenticado(email);
        if (favoritos.deleteByUsuarioIdAndFilmeId(usuario.getId(), tmdbId) == 0)
            throw new ApiException(HttpStatus.NOT_FOUND, "Filme não consta nos seus favoritos");
    }
}
