package com.neueda.url.app.service;

import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.model.UrlStatistics;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    UrlResponse persistentUrlDetails(String url);

    UrlResponse updateAndGetUrlStatistics(String urlIdentifier);

    UrlStatistics getUrlStatisticsByUrlIdentifier(String urlIdentifier);
}
