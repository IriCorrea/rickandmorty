package com.api.rickandmorty.service;

import com.api.rickandmorty.client.RickAndMortyClient;
import com.api.rickandmorty.client.response.CharacterResponse;
import com.api.rickandmorty.mapper.RickAndMortyMapper;
import com.api.rickandmorty.model.PaginaPersonagem;
import com.api.rickandmorty.model.PersonagemResponse;
import com.api.rickandmorty.util.TestMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RickAndMortyServiceTest {

    @Mock
    private RickAndMortyClient client;

    @Mock
    private RickAndMortyMapper mapper;

    @InjectMocks
    private RickAndMortyService service;

    @Test
    void listarPersonagens_DeveRetornarPaginaPreenchida_QuandoClientRetornarDados() {
        // Arrange
        when(client.listarPersonagens(anyInt())).thenReturn(Optional.of(TestMocks.criarCharacterListResponse()));
        when(mapper.toResponse(any(CharacterResponse.class))).thenReturn(TestMocks.criarPersonagemResponse());

        // Act
        PaginaPersonagem resultado = service.listarPersonagens(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getPage());
        assertFalse(resultado.getResults().isEmpty());
        assertEquals(TestMocks.NOME_PADRAO, resultado.getResults().getFirst().getNome());
    }

    @Test
    void listarPersonagens_DeveRetornarPaginaVazia_QuandoClientRetornarVazio() {
        // Arrange
        when(client.listarPersonagens(anyInt())).thenReturn(Optional.empty());

        // Act
        PaginaPersonagem resultado = service.listarPersonagens(1);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getResults().isEmpty());
    }

    @Test
    void buscarPorNome_DeveRetornarPersonagem_QuandoEncontrado() {
        // Arrange
        when(client.buscarPorNome(anyString())).thenReturn(Optional.of(TestMocks.criarCharacterListResponse()));
        when(mapper.toResponse(any(CharacterResponse.class))).thenReturn(TestMocks.criarPersonagemResponse());

        // Act
        PersonagemResponse resultado = service.buscarPorNome("Rick");

        // Assert
        assertNotNull(resultado);
        assertEquals(TestMocks.NOME_PADRAO, resultado.getNome());
    }

    @Test
    void buscarPorNome_DeveLancarExcecao_QuandoNaoEncontrado() {
        // Arrange
        when(client.buscarPorNome(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            service.buscarPorNome("Ninguém")
        );
        
        assertEquals("Personagem não encontrado", exception.getMessage());
    }
}
