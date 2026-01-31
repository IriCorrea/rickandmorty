package com.api.rickandmorty.client;

import com.api.rickandmorty.client.response.CharacterListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Component
public class RickAndMortyClient {

    private final RestClient restClient;

    public RickAndMortyClient(RestClient.Builder builder, @Value("${rickandmorty.api.url:https://rickandmortyapi.com/api}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public Optional<CharacterListResponse> listarPersonagens(Integer page) {
        log.info("Chamando API externa para listar personagens. Página: {}", page);
        try {
            return Optional.ofNullable(restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/character").queryParam("page", page).build())
                    .retrieve()
                    .body(CharacterListResponse.class));
        } catch (Exception e) {
            log.error("Erro ao chamar API externa (listarPersonagens): {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<CharacterListResponse> buscarPorNome(String nome) {
        log.info("Chamando API externa para buscar personagem por nome: {}", nome);
        try {
            return Optional.ofNullable(restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/character").queryParam("name", nome).build())
                    .retrieve()
                    .body(CharacterListResponse.class));
        } catch (Exception e) {
            log.error("Erro ao chamar API externa (buscarPorNome): {}", e.getMessage());
            return Optional.empty();
        }
    }
}
