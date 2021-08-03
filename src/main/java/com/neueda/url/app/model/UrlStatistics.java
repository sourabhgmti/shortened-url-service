package com.neueda.url.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlStatistics {
    private String url;
    private String shortenedUrl;
    private String createdOn;
    private String totalNumberOfHits;
    private String lastAccessedOn;
}
