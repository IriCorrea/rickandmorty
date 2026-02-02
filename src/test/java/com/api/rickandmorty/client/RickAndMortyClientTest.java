package com.api.rickandmorty.client;

import com.api.rickandmorty.client.response.CharacterListResponse;
import com.api.rickandmorty.util.TestMocks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.http.HttpMethod.GET;

@DisplayName("Unidade: RickAndMortyClient")
class RickAndMortyClientTest {

    private RickAndMortyClient client;
    private MockRestServiceServer server;
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "https://rickandmortyapi.com/api";

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        objectMapper = new ObjectMapper();
        
        client = new RickAndMortyClient(builder, BASE_URL);
    }

    @Nested
    @DisplayName("Método: listarPersonagens(page)")
    class ListarPersonagens {

        @Test
        @DisplayName("Deve retornar lista quando API responder com 200 OK")
        void deveRetornarLista_QuandoApiResponder200() throws JsonProcessingException {
            // Arrange
            CharacterListResponse mockResponse = TestMocks.criarCharacterListResponse();
            String jsonResponse = objectMapper.writeValueAsString(mockResponse);

            server.expect(requestTo(BASE_URL + "/character?page=1"))
                    .andExpect(method(GET))
                    .andExpect(queryParam("page", "1"))
                    .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

            // Act
            Optional<CharacterListResponse> result = client.listarPersonagens(1);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(mockResponse.getResults().size(), result.get().getResults().size());
            assertEquals(mockResponse.getResults().getFirst().getName(), result.get().getResults().getFirst().getName());
            
            server.verify();
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando API responder com 404 Not Found")
        void deveRetornarVazio_QuandoApiResponder404() {
            // Arrange
            server.expect(requestTo(BASE_URL + "/character?page=999"))
                    .andRespond(withResourceNotFound());

            // Act
            Optional<CharacterListResponse> result = client.listarPersonagens(999);

            // Assert
            assertTrue(result.isEmpty());
            server.verify();
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando API responder com 500 Server Error")
        void deveRetornarVazio_QuandoApiResponder500() {
            // Arrange
            server.expect(requestTo(BASE_URL + "/character?page=1"))
                    .andRespond(withServerError());

            // Act
            Optional<CharacterListResponse> result = client.listarPersonagens(1);

            // Assert
            assertTrue(result.isEmpty());
            server.verify();
        }
    }

    @Nested
    @DisplayName("Método: buscarPorNome(nome)")
    class BuscarPorNome {

        @Test
        @DisplayName("Deve retornar lista quando API responder com 200 OK")
        void deveRetornarLista_QuandoApiResponder200() throws JsonProcessingException {
            // Arrange
            String nomeBusca = "Rick";
            CharacterListResponse mockResponse = TestMocks.criarCharacterListResponse();
            String jsonResponse = objectMapper.writeValueAsString(mockResponse);

            server.expect(requestTo(BASE_URL + "/character?name=" + nomeBusca))
                    .andExpect(method(GET))
                    .andExpect(queryParam("name", nomeBusca))
                    .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

            // Act
            Optional<CharacterListResponse> result = client.buscarPorNome(nomeBusca);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(mockResponse.getResults().getFirst().getName(), result.get().getResults().getFirst().getName());
            server.verify();
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando API responder com 404 Not Found")
        void deveRetornarVazio_QuandoApiResponder404() {
            // Arrange
            String nomeBusca = "Ninguem";
            server.expect(requestTo(BASE_URL + "/character?name=" + nomeBusca))
                    .andRespond(withResourceNotFound());

            // Act
            Optional<CharacterListResponse> result = client.buscarPorNome(nomeBusca);

            // Assert
            assertTrue(result.isEmpty());
            server.verify();
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando API lançar exceção inesperada")
        void deveRetornarVazio_QuandoOcorrerExcecaoInesperada() {
            // Arrange
            server.expect(requestTo(BASE_URL + "/character?name=Error"))
                    .andRespond(withServerError());

            // Act
            Optional<CharacterListResponse> result = client.buscarPorNome("Error");

            // Assert
            assertTrue(result.isEmpty());
            server.verify();
        }
    }
}
