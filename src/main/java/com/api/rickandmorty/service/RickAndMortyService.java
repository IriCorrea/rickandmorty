package com.api.rickandmorty.service;

import com.api.rickandmorty.client.RickAndMortyClient;
import com.api.rickandmorty.client.response.CharacterListResponse;
import com.api.rickandmorty.mapper.RickAndMortyMapper;
import com.api.rickandmorty.model.PaginaPersonagem;
import com.api.rickandmorty.model.PersonagemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RickAndMortyService {

    private final RickAndMortyClient client;
    private final RickAndMortyMapper mapper;

    @Cacheable("personagens")
    public PaginaPersonagem listarPersonagens(Integer page) {
        log.info("Iniciando serviço de listagem de personagens. Página: {}", page);

        return client.listarPersonagens(page)
                .map(response -> {
                    List<PersonagemResponse> mappedResults = response.getResults().stream()
                            .map(mapper::toResponse)
                            .toList();
                    
                    log.info("Listagem concluída. {} personagens encontrados.", mappedResults.size());
                    
                    return new PaginaPersonagem()
                            .page(page)
                            .results(mappedResults);
                })
                .orElseGet(() -> {
                    log.warn("Nenhum resultado encontrado na API externa para a página {}", page);
                    return new PaginaPersonagem()
                            .page(page)
                            .results(Collections.emptyList());
                });
    }

    @Cacheable("personagemPorNome")
    public PersonagemResponse buscarPorNome(String nome) {
        log.info("Iniciando busca de personagem por nome: {}", nome);

        return client.buscarPorNome(nome)
                .map(CharacterListResponse::getResults)
                .filter(results -> !results.isEmpty())
                .map(results -> results.get(0))
                .map(firstMatch -> {
                    log.info("Personagem encontrado: ID={}, Nome={}", firstMatch.getId(), firstMatch.getName());
                    return mapper.toResponse(firstMatch);
                })
                .orElseThrow(() -> {
                    log.warn("Personagem '{}' não encontrado na API externa.", nome);
                    return new RuntimeException("Personagem não encontrado");
                });
    }
}
