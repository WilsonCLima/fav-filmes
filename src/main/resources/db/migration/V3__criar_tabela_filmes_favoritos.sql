CREATE TABLE usuario_favoritos (
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    filme_id BIGINT NOT NULL REFERENCES filmes(id) ON DELETE CASCADE,
    favorited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, filme_id)
);
CREATE INDEX idx_usuario_favoritos_usuario ON usuario_favoritos(usuario_id);
