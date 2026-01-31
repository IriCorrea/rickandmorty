package com.api.rickandmorty.controller;

import com.api.rickandmorty.api.PersonagensApi;
import com.api.rickandmorty.model.PaginaPersonagem;
import com.api.rickandmorty.model.PersonagemResponse;
import com.api.rickandmorty.service.RickAndMortyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonagemController implements PersonagensApi {

    private final RickAndMortyService service;

    @Override
    public ResponseEntity<PaginaPersonagem> listarPersonagens(Integer page) {
        log.info("Requisição recebida: GET /personagens?page={}", page);
        return ResponseEntity.ok(service.listarPersonagens(page));
    }

    @Override
    public ResponseEntity<PersonagemResponse> buscarPorNome(String nome) {
        log.info("Requisição recebida: GET /personagens/{}", nome);
        try {
            return ResponseEntity.ok(service.buscarPorNome(nome));
        } catch (RuntimeException e) {
            log.warn("Personagem não encontrado: {}", nome);
            return ResponseEntity.notFound().build();
        }
    }
}
