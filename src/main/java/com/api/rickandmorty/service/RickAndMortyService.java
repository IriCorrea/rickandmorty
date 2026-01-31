package com.api.rickandmorty.service;

import com.api.rickandmorty.client.RickAndMortyClient;
import com.api.rickandmorty.client.response.CharacterListResponse;
import com.api.rickandmorty.client.response.CharacterResponse;
import com.api.rickandmorty.model.PaginaPersonagem;
import com.api.rickandmorty.model.PersonagemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RickAndMortyService {

    private final RickAndMortyClient client;

    @Cacheable("personagens")
    public PaginaPersonagem listarPersonagens(Integer page) {
        log.info("Iniciando serviço de listagem de personagens. Página: {}", page);
        Optional<CharacterListResponse> responseOpt = client.listarPersonagens(page);

        if (responseOpt.isEmpty()) {
            log.warn("Nenhum resultado encontrado na API externa para a página {}", page);
            PaginaPersonagem emptyPage = new PaginaPersonagem();
            emptyPage.setPage(page);
            emptyPage.setResults(Collections.emptyList());
            return emptyPage;
        }

        CharacterListResponse response = responseOpt.get();
        PaginaPersonagem pagina = new PaginaPersonagem();
        pagina.setPage(page);
        
        List<PersonagemResponse> mappedResults = response.getResults().stream()
                .map(this::mapToPersonagemResponse)
                .collect(Collectors.toList());
        
        pagina.setResults(mappedResults);
        log.info("Listagem concluída. {} personagens encontrados.", mappedResults.size());
        return pagina;
    }

    @Cacheable("personagemPorNome")
    public PersonagemResponse buscarPorNome(String nome) {
        log.info("Iniciando busca de personagem por nome: {}", nome);
        // A API retorna uma lista de personagens que dão match com o nome.
        // O requisito pede para retornar UM objeto. Vamos pegar o primeiro da lista.
        Optional<CharacterListResponse> responseOpt = client.buscarPorNome(nome);

        if (responseOpt.isEmpty() || responseOpt.get().getResults() == null || responseOpt.get().getResults().isEmpty()) {
            log.warn("Personagem '{}' não encontrado na API externa.", nome);
            throw new RuntimeException("Personagem não encontrado"); // Idealmente uma Custom Exception
        }

        CharacterResponse firstMatch = responseOpt.get().getResults().get(0);
        log.info("Personagem encontrado: ID={}, Nome={}", firstMatch.getId(), firstMatch.getName());
        return mapToPersonagemResponse(firstMatch);
    }

    private PersonagemResponse mapToPersonagemResponse(CharacterResponse source) {
        PersonagemResponse target = new PersonagemResponse();
        target.setId(source.getId());
        target.setNome(source.getName());
        return target;
    }
}
