package com.neueda.url.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlResponse {
    private String url;
    private String shortenedUrl;
}
