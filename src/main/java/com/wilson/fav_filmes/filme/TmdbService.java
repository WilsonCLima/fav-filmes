package com.wilson.fav_filmes.filme;

import com.wilson.fav_filmes.infra.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TmdbService {

    private final RestClient restClient;
    private final String apiKey;

    public TmdbService(RestClient.Builder builder,
                       @Value("${tmdb.api.key}") String apiKey,
                       @Value("${tmdb.api.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    private void verificarConfiguracao() {
        if (apiKey.isBlank()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "TMDB_API_KEY não configurada");
        }
    }

    public TmdbPagina populares(int pagina) {
        verificarConfiguracao();
        return restClient.get().uri(uri -> uri.path("/movie/popular")
                .queryParam("api_key", apiKey).queryParam("language", "pt-BR")
                .queryParam("page", pagina).build())
            .retrieve().body(TmdbPagina.class);
    }

    public TmdbPagina buscar(String consulta, int pagina) {
        verificarConfiguracao();
        return restClient.get().uri(uri -> uri.path("/search/movie")
                .queryParam("api_key", apiKey).queryParam("language", "pt-BR")
                .queryParam("query", consulta).queryParam("page", pagina).build())
            .retrieve().body(TmdbPagina.class);
    }

    public TmdbFilme detalhar(Long id) {
        verificarConfiguracao();
        try {
            return restClient.get().uri(uri -> uri.path("/movie/{id}")
                    .queryParam("api_key", apiKey).queryParam("language", "pt-BR").build(id))
                .retrieve().body(TmdbFilme.class);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Filme não encontrado no TMDB");
        }
    }
}
