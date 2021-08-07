package com.neueda.url.app.web.controller;

import com.neueda.url.app.model.UrlRequest;
import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.model.UrlStatistics;
import com.neueda.url.app.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class ShortenedUrlController {

    private final UrlService urlServiceImpl;

    /**
     *
     * @param urlRequest
     * @return
     */
    @PostMapping("/short")
    public ResponseEntity<UrlResponse> generateAndGetShortURL(@RequestBody UrlRequest urlRequest) {
        UrlResponse urlResponse = urlServiceImpl.persistentUrlDetails(urlRequest.getUrl());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, urlResponse.getShortenedUrl())
                .body(urlResponse);
    }

    /**
     *
     * @param urlIdentifier
     * @return
     */
    @GetMapping("/{urlIdentifier}")
    public ResponseEntity<Object> redirectToActualUrl(@PathVariable String urlIdentifier) {
        UrlResponse urlResponse=urlServiceImpl.updateAndGetUrlStatistics(urlIdentifier);
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, urlResponse.getUrl())
                .build();
    }

    /**
     * 
     * @param urlIdentifier
     * @return
     */
    @GetMapping("/{urlIdentifier}/stat")
    public ResponseEntity<Object> getUrlStatisticsByUrlIdentifier(@PathVariable String urlIdentifier) {
        UrlStatistics urlStatistics=urlServiceImpl.getUrlStatisticsByUrlIdentifier(urlIdentifier);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(urlStatistics);
    }
}
