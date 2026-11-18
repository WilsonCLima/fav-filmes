package com.wilson.fav_filmes.infra;

import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> api(ApiException ex) {
        var problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacao(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos");
        problem.setProperty("campos", ex.getBindingResult().getFieldErrors().stream()
            .map(e -> Map.of("campo", e.getField(), "erro", e.getDefaultMessage() == null ? "inválido" : e.getDefaultMessage())).toList());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> autenticacao(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ProblemDetail> tmdb(RestClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "Falha na comunicação com TMDB"));
    }
}
