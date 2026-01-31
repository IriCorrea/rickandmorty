package com.api.rickandmorty.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterListResponse {
    private Info info;
    private List<CharacterResponse> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        private Integer count;
        private Integer pages;
        private String next;
        private String prev;
    }
}
