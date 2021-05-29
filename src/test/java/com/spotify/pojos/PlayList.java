package com.spotify.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) @JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayList {
    private boolean collaborative;
    private String description;
    private String id;
    private String name;
    @JsonProperty("owner")
    private Owner user;
    @JsonProperty("public")
    private boolean isPublic;
}
