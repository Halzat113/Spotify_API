package com.spotify.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Items {
    private String id;
    public String name;
    public boolean track;
    public String uri;
}
