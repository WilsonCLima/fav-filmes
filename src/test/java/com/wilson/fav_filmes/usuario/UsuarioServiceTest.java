package com.wilson.fav_filmes.usuario;

import com.wilson.fav_filmes.infra.ApiException;
import com.wilson.fav_filmes.usuario.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository repository;
    @Mock PasswordEncoder encoder;
    UsuarioService service;

    @BeforeEach void iniciar() {
        service = new UsuarioService(repository, new UsuarioAssembler(), encoder);
    }

    @Test void deveCadastrarUsuarioComSenhaCodificadaERoleUser() {
        when(repository.existsByEmailIgnoreCase("teste@exemplo.com")).thenReturn(false);
        when(encoder.encode("senhasegura")).thenReturn("hash-bcrypt");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });
        var resultado = service.cadastrar(new UsuarioInput("Teste@Exemplo.com", "senhasegura"));
        assertEquals("teste@exemplo.com", resultado.email());
        assertEquals(Role.USER, resultado.role());
        var captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals("hash-bcrypt", captor.getValue().getSenha());
    }

    @Test void naoDeveCadastrarEmailDuplicado() {
        when(repository.existsByEmailIgnoreCase("existente@exemplo.com")).thenReturn(true);
        assertThrows(ApiException.class,
            () -> service.cadastrar(new UsuarioInput("existente@exemplo.com", "senhasegura")));
        verify(repository, never()).save(any());
    }
}
