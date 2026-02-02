package com.api.rickandmorty.client.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Unidade: CharacterListResponse (JSON)")
class CharacterListResponseTest {

    @Autowired
    private JacksonTester<CharacterListResponse> json;

    @Test
    @DisplayName("Deve desserializar JSON completo corretamente")
    void deveDesserializarJsonCompleto() throws IOException {
        // Arrange
        String jsonContent = """
            {
              "info": {
                "count": 826,
                "pages": 42,
                "next": "https://rickandmortyapi.com/api/character?page=2",
                "prev": null
              },
              "results": [
                {
                  "id": 1,
                  "name": "Rick Sanchez",
                  "status": "Alive",
                  "species": "Human",
                  "type": "",
                  "gender": "Male",
                  "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                  "url": "https://rickandmortyapi.com/api/character/1",
                  "created": "2017-11-04T18:48:46.250Z"
                }
              ]
            }
            """;

        // Act
        CharacterListResponse response = json.parseObject(jsonContent);

        // Assert
        assertThat(response).isNotNull();
        

        assertThat(response.getInfo()).isNotNull();
        assertThat(response.getInfo().getCount()).isEqualTo(826);
        assertThat(response.getInfo().getPages()).isEqualTo(42);
        assertThat(response.getInfo().getNext()).isEqualTo("https://rickandmortyapi.com/api/character?page=2");
        assertThat(response.getInfo().getPrev()).isNull();


        assertThat(response.getResults()).hasSize(1);
        CharacterResponse character = response.getResults().getFirst();
        assertThat(character.getId()).isEqualTo(1);
        assertThat(character.getName()).isEqualTo("Rick Sanchez");
        assertThat(character.getStatus()).isEqualTo("Alive");
        assertThat(character.getSpecies()).isEqualTo("Human");
        assertThat(character.getGender()).isEqualTo("Male");
    }

    @Test
    @DisplayName("Deve ignorar campos desconhecidos no JSON")
    void deveIgnorarCamposDesconhecidos() throws IOException {
        // Arrange
        String jsonContent = """
            {
              "campo_estranho": "valor",
              "results": []
            }
            """;

        // Act
        CharacterListResponse response = json.parseObject(jsonContent);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getResults()).isEmpty();
    }

    @Test
    @DisplayName("Deve serializar objeto Java para JSON corretamente")
    void deveSerializarParaJson() throws IOException {
        // Arrange
        CharacterListResponse.Info info = new CharacterListResponse.Info();
        info.setCount(10);
        info.setPages(1);

        CharacterResponse character = new CharacterResponse();
        character.setId(1);
        character.setName("Morty");

        CharacterListResponse response = new CharacterListResponse();
        response.setInfo(info);
        response.setResults(List.of(character));

        // Act
        JsonContent<CharacterListResponse> result = json.write(response);

        // Assert
        assertThat(result).hasJsonPathNumberValue("$.info.count", 10);
        assertThat(result).hasJsonPathNumberValue("$.results[0].id", 1);
        assertThat(result).hasJsonPathStringValue("$.results[0].name", "Morty");
    }
}
