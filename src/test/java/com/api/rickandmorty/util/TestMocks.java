package com.api.rickandmorty.util;

import com.api.rickandmorty.client.response.CharacterListResponse;
import com.api.rickandmorty.client.response.CharacterResponse;
import com.api.rickandmorty.model.PaginaPersonagem;
import com.api.rickandmorty.model.PersonagemResponse;

import java.util.List;

public class TestMocks {

    public static final Integer ID_PADRAO = 1;
    public static final String NOME_PADRAO = "Rick Sanchez";

    public static CharacterResponse criarCharacterResponse() {
        CharacterResponse response = new CharacterResponse();
        response.setId(ID_PADRAO);
        response.setName(NOME_PADRAO);
        return response;
    }

    public static CharacterListResponse criarCharacterListResponse() {
        CharacterListResponse listResponse = new CharacterListResponse();
        listResponse.setResults(List.of(criarCharacterResponse()));
        return listResponse;
    }

    public static PersonagemResponse criarPersonagemResponse() {
        return new PersonagemResponse()
                .id(ID_PADRAO)
                .nome(NOME_PADRAO);
    }

    public static PaginaPersonagem criarPaginaPersonagem() {
        return new PaginaPersonagem()
                .page(1)
                .results(List.of(criarPersonagemResponse()));
    }
}
