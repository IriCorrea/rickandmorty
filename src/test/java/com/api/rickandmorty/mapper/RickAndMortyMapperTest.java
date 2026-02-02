package com.api.rickandmorty.mapper;

import com.api.rickandmorty.client.response.CharacterResponse;
import com.api.rickandmorty.model.PersonagemResponse;
import com.api.rickandmorty.util.TestMocks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unidade: RickAndMortyMapper")
class RickAndMortyMapperTest {

    private final RickAndMortyMapper mapper = Mappers.getMapper(RickAndMortyMapper.class);

    @Test
    @DisplayName("Deve mapear CharacterResponse para PersonagemResponse corretamente")
    void deveMapearCharacterParaPersonagem() {
        // Arrange
        CharacterResponse source = TestMocks.criarCharacterResponse();

        // Act
        PersonagemResponse target = mapper.toResponse(source);

        // Assert
        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getName(), target.getNome()); // Valida o @Mapping(source="name", target="nome")
    }

    @Test
    @DisplayName("Deve retornar null quando a entrada for null")
    void deveRetornarNull_QuandoEntradaNull() {
        // Act
        PersonagemResponse target = mapper.toResponse(null);

        // Assert
        assertNull(target);
    }
}
