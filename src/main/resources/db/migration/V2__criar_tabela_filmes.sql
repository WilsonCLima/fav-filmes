CREATE TABLE filmes (
    id BIGINT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    sinopse TEXT,
    data_lancamento DATE,
    poster_path VARCHAR(255)
);
