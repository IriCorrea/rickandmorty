package com.api.rickandmorty.controller;

import com.api.rickandmorty.service.RickAndMortyService;
import com.api.rickandmorty.util.TestMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonagemController.class)
class PersonagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RickAndMortyService service;

    @Test
    void listarPersonagens_DeveRetornar200_QuandoSucesso() throws Exception {
        when(service.listarPersonagens(anyInt())).thenReturn(TestMocks.criarPaginaPersonagem());

        mockMvc.perform(get("/personagens")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.results[0].nome").value(TestMocks.NOME_PADRAO));
    }

    @Test
    void buscarPorNome_DeveRetornar200_QuandoEncontrado() throws Exception {
        when(service.buscarPorNome(anyString())).thenReturn(TestMocks.criarPersonagemResponse());

        mockMvc.perform(get("/personagens/Rick")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(TestMocks.NOME_PADRAO));
    }

    @Test
    void buscarPorNome_DeveRetornar404_QuandoNaoEncontrado() throws Exception {
        when(service.buscarPorNome(anyString())).thenThrow(new RuntimeException("Personagem não encontrado"));

        mockMvc.perform(get("/personagens/Ninguem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
