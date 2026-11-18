package com.wilson.fav_filmes.filme;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record TmdbFilme(Long id,
                       String title,
                       String overview,
                       @JsonProperty("release_date") LocalDate releaseDate,
                       @JsonProperty("poster_path") String posterPath,
                       @JsonProperty("vote_average") Double voteAverage) {}
