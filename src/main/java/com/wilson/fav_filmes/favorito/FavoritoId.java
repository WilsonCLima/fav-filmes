package com.wilson.fav_filmes.favorito;
import java.io.Serializable;
import java.util.Objects;
public class FavoritoId implements Serializable {

    private Long usuario;
    private Long filme;
    public FavoritoId() {}
    public FavoritoId(Long usuario, Long filme) { this.usuario=usuario; this.filme=filme; }
    @Override public boolean equals(Object o) {
        if (this==o) {
            return true;
        }
        if (!(o instanceof FavoritoId that)) {
            return false;
        }
        return Objects.equals(usuario, that.usuario) && Objects.equals(filme, that.filme);
    }

    @Override public int hashCode() {

        return Objects.hash(usuario, filme);

    }
}
