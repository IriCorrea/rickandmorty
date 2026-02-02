package com.api.rickandmorty.integration;

import com.api.rickandmorty.client.response.CharacterListResponse;
import com.api.rickandmorty.util.TestMocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DisplayName("Integração: Fluxo Completo (E2E)")
class FluxoCompletoIntegradoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve realizar fluxo completo de listagem: Controller -> Service -> Client -> API Externa")
    void deveListarPersonagensFluxoCompleto() throws Exception {

        CharacterListResponse externalResponse = TestMocks.criarCharacterListResponse();
        String jsonExternalResponse = objectMapper.writeValueAsString(externalResponse);


        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character?page=1"))
                .andExpect(method(GET))
                .andRespond(withSuccess(jsonExternalResponse, MediaType.APPLICATION_JSON));


        mockMvc.perform(get("/personagens")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].id").value(TestMocks.ID_PADRAO))
                .andExpect(jsonPath("$.results[0].nome").value(TestMocks.NOME_PADRAO));

        // 3. Verificar se a API externa foi realmente chamada
        mockServer.verify();
    }

    @Test
    @DisplayName("Deve realizar fluxo completo de busca por nome")
    void deveBuscarPorNomeFluxoCompleto() throws Exception {
        // 1. Preparar o Mock da API Externa
        CharacterListResponse externalResponse = TestMocks.criarCharacterListResponse();
        String jsonExternalResponse = objectMapper.writeValueAsString(externalResponse);

        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character?name=Rick"))
                .andExpect(method(GET))
                .andRespond(withSuccess(jsonExternalResponse, MediaType.APPLICATION_JSON));

        // 2. Executar a chamada na Nossa API
        mockMvc.perform(get("/personagens/Rick")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TestMocks.ID_PADRAO))
                .andExpect(jsonPath("$.nome").value(TestMocks.NOME_PADRAO));

        // 3. Verificar
        mockServer.verify();
    }
}
