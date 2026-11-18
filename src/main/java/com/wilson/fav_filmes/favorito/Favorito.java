package com.wilson.fav_filmes.favorito;

import com.wilson.fav_filmes.usuario.Usuario;
import com.wilson.fav_filmes.filme.Filme;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="usuario_favoritos")
@IdClass(FavoritoId.class)
@Getter
@Setter
@NoArgsConstructor
public class Favorito {

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="filme_id")
    private Filme filme;

    @Column(name="favorited_at", nullable=false)
    private LocalDateTime favoritedAt;

    @PrePersist void criar() {
        if (favoritedAt==null) favoritedAt=LocalDateTime.now();
    }

    public Favorito(Usuario usuario, Filme filme) {
        this.usuario=usuario; this.filme=filme;
    }
}
