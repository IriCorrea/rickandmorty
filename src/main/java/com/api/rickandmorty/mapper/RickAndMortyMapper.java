package com.api.rickandmorty.mapper;

import com.api.rickandmorty.client.response.CharacterResponse;
import com.api.rickandmorty.model.PersonagemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RickAndMortyMapper {

    @Mapping(source = "name", target = "nome")
    PersonagemResponse toResponse(CharacterResponse source);
}
