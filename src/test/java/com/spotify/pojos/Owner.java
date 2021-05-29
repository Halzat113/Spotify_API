package com.spotify.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Owner {
    @JsonProperty("display_name")
    private String user_name;
    @JsonProperty("id")
    private String user_id;
    private String uri;
}
