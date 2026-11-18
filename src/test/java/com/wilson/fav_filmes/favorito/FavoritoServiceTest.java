package com.wilson.fav_filmes.favorito;

import com.wilson.fav_filmes.filme.*;
import com.wilson.fav_filmes.usuario.*;
import com.wilson.fav_filmes.infra.ApiException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {
    @Mock UsuarioService usuarios;
    @Mock FilmeRepository filmes;
    @Mock FavoritoRepository favoritos;
    @Mock TmdbService tmdb;
    FavoritoService service;

    @BeforeEach void iniciar() {
        service = new FavoritoService(usuarios, filmes, favoritos, tmdb);
    }

    @Test void naoDeveAdicionarFilmeRepetido() {
        Usuario usuario = new Usuario();
        usuario.setId(4L);
        when(usuarios.buscarAutenticado("teste@exemplo.com")).thenReturn(usuario);
        when(favoritos.existsByUsuarioIdAndFilmeId(4L, 10L)).thenReturn(true);
        assertThrows(ApiException.class, () -> service.adicionar("teste@exemplo.com", 10L));
        verifyNoInteractions(filmes, tmdb);
    }

    @Test void naoDeveRemoverFavoritoDeOutroUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(4L);
        when(usuarios.buscarAutenticado("teste@exemplo.com")).thenReturn(usuario);
        when(favoritos.deleteByUsuarioIdAndFilmeId(4L, 10L)).thenReturn(0L);
        assertThrows(ApiException.class, () -> service.remover("teste@exemplo.com", 10L));
    }
}
