package com.neueda.url.app.web.controller;


import com.neueda.url.app.ShortenedUrlApplication;
import com.neueda.url.app.entities.UrlEntity;
import com.neueda.url.app.mapstruct.UrlMapStructMapper;
import com.neueda.url.app.message.ServiceMessage;
import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.model.UrlStatistics;
import com.neueda.url.app.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ShortenedUrlApplication.class})
@ActiveProfiles("test")
public class ShortenedUrlControllerTest {

    @Autowired
    public MessageSource messageSource;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlMapStructMapper urlMapStructMapper;

    @MockBean
    private ShortenedUrlRepository shortenedUrlRepository;

    @Test
    public void testGenerateAndGetShortURL() throws Exception {
        doReturn(getMockedUrlResponse()).when(urlMapStructMapper).toUrlResponse(any());
        doReturn(getMockedUrlEntity()).when(shortenedUrlRepository).getShortenedUrlByUrl(any());
        mockMvc.perform(
                        post("/url/short")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .content(getJsonStringFromResource("test/json/UrlRequest.json")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url", containsString("http://bit.ly/SaaYw5")))
                .andExpect(jsonPath("$.shortenedUrl", containsString("http://short")));
    }

    @Test
    public void testGenerateAndGetShortURLWhenUrlIsBlank() throws Exception {
        mockMvc.perform(
                        post("/url/short")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .content(getJsonStringFromResource("test/json/BlankUrlRequest.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(messageSource.getMessage(ServiceMessage.ValidationMessageSourceCode.URL_NOT_EMPTY, null, Locale.ENGLISH))));
    }

    @Test
    public void testGenerateAndGetShortURLWhenUrlIsMalformed() throws Exception {
        mockMvc.perform(
                        post("/url/short")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .content(getJsonStringFromResource("test/json/MalformedUrlRequest.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(messageSource.getMessage(ServiceMessage.ValidationMessageSourceCode.URL_NOT_VALID, null, Locale.ENGLISH))));
    }

    @Test
    public void testGenerateAndGetShortURLWhenUnexpectedException() throws Exception {
        doThrow(new RuntimeException()).when(urlMapStructMapper).toUrlResponse(any());
        mockMvc.perform(
                        post("/url/short")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .content(getJsonStringFromResource("test/json/UrlRequest.json")))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", containsString(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_SERVICE_EXCEPTION, null, Locale.ENGLISH))));
    }

    @Test
    public void testRedirectToActualUrlForExistingEntity() throws Exception {
        doReturn(getMockedUrlEntity()).when(shortenedUrlRepository).getUrlEntityByUrlIdentifier(any());
        doReturn(getMockedUrlResponse()).when(urlMapStructMapper).toUrlResponse(any());
        mockMvc.perform(
                        get("/url/DESKTOP-KMGDPMC-1627910318573-1627910403703-0"))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("http://bit.ly/SaaYw5"));
    }

    @Test
    public void testRedirectToActualUrlForNonExistingEntity() throws Exception {
        doReturn(getMockedUrlEntity()).when(shortenedUrlRepository).getUrlEntityByUrlIdentifier(any());
        mockMvc.perform(
                        get("/url/DESKTOP-KMGDPMC-1627910318573-1627910403703-0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_NOT_FOUND_EXCEPTION, null, Locale.ENGLISH))));
    }

    @Test
    public void testGetUrlStatisticsByUrlIdentifierForNonExistingEntity() throws Exception {
        mockMvc.perform(
                        get("/url/DESKTOP-KMGDPMC-1627910318573-1627910403703-0/stat"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_NOT_FOUND_EXCEPTION, null, Locale.ENGLISH))));
    }

    @Test
    public void testGetUrlStatisticsByUrlIdentifierForExistingEntity() throws Exception {
        doReturn(getMockedUrlUrlStatistics()).when(urlMapStructMapper).toUrlStatistics(any());
        mockMvc.perform(
                        get("/url/DESKTOP-KMGDPMC-1627910318573-1627910403703-0/stat"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalNumberOfHits", containsString("5")));
    }

    private String getJsonStringFromResource(String jsonFilePath) throws IOException {
        File resource = new ClassPathResource(jsonFilePath).getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }

    private UrlStatistics getMockedUrlUrlStatistics() {
        UrlStatistics urlStatistics = new UrlStatistics();
        urlStatistics.setShortenedUrl("http://short");
        urlStatistics.setUrl("http://bit.ly/SaaYw5");
        urlStatistics.setCreatedOn(new Date().toString());
        urlStatistics.setLastAccessedOn(new Date().toString());
        urlStatistics.setTotalNumberOfHits("5");
        return urlStatistics;
    }

    private UrlEntity getMockedUrlEntity() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setCreatedOn(new Date());
        urlEntity.setTotalNumberOfHits(0);
        urlEntity.setShortenedUrl("http://short");
        urlEntity.setUrl("http://bit.ly/SaaYw5");
        return urlEntity;
    }

    private UrlResponse getMockedUrlResponse() {
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setShortenedUrl("http://short");
        urlResponse.setUrl("http://bit.ly/SaaYw5");
        return urlResponse;
    }
}
