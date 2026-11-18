package com.wilson.fav_filmes.filme;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
public record TmdbPagina(int page, List<TmdbFilme> results,
    @JsonProperty("total_pages") int totalPages,
    @JsonProperty("total_results") int totalResults) {}
