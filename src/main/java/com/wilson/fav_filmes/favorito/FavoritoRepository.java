package com.wilson.fav_filmes.favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    List<Favorito> findByUsuarioIdOrderByFavoritedAtDesc(Long usuarioId);
    boolean existsByUsuarioIdAndFilmeId(Long usuarioId, Long filmeId);
    long deleteByUsuarioIdAndFilmeId(Long usuarioId, Long filmeId);
}
