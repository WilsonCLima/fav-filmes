package com.wilson.fav_filmes.filme;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name="filmes")
@Getter
@Setter
@NoArgsConstructor
public class Filme {

    @Id
    private Long id;

    @Column(name="titulo", nullable=false)
    private String titulo;

    @Column(name="sinopse", columnDefinition="TEXT")
    private String sinopse;

    @Column(name="data_lancamento")
    private LocalDate dataLancamento;

    @Column(name="poster_path")
    private String posterPath;
}
