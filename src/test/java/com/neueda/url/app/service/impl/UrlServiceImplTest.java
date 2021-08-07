package com.neueda.url.app.service.impl;

import com.neueda.url.app.AbstractTest;
import com.neueda.url.app.entities.UrlEntity;
import com.neueda.url.app.exception.ApiServiceException;
import com.neueda.url.app.factory.impl.IdFactoryImpl;
import com.neueda.url.app.mapstruct.UrlMapStructMapper;
import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.repository.ShortenedUrlRepository;
import com.neueda.url.app.validation.RequestValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class UrlServiceImplTest extends AbstractTest {

    public static final String UNEXPECTED_EXCEPTION_OCCURRED = "Unexpected exception occurred.";
    public static final String API_SERVICE_EXCEPTION_OCCURRED = "Api service exception occurred.";

    @InjectMocks
    private UrlServiceImpl UrlServiceImpl;

    @Mock
    private ShortenedUrlRepository shortenedUrlRepository;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private UrlMapStructMapper urlMapStructMapper;

    @Mock
    private IdFactoryImpl idFactoryImpl;

    @Mock
    private MessageSource messageSource;

    /**
     * When url is already exists into database
     * or user is trying to generate short url from same that is already exists into system
     */
    @Test
    public void testPersistentUrlDetailsIfAlreadyExists() {
        when(shortenedUrlRepository.getShortenedUrlByUrl(anyString())).thenReturn(getMockedUrlEntityObj());
        when(urlMapStructMapper.toUrlResponse(any())).thenReturn(getMockedUrlResponseObj());
        UrlResponse urlResponse = UrlServiceImpl.persistentUrlDetails("http://bit.ly/SaaYw5");
        assertEquals("http://bit.ly/SaaYw5", urlResponse.getUrl());
        assertEquals("http://short", urlResponse.getShortenedUrl());
    }

    /**
     * New record with unique id would be created in case of new url that doesn't exists
     * into system
     */
    @Test
    public void testPersistentUrlDetailsWhenUrlIsSaved() {
        when(shortenedUrlRepository.save(any())).thenReturn(getMockedUrlEntityObj());
        when(urlMapStructMapper.toUrlResponse(any())).thenReturn(getMockedUrlResponseObj());
        //New id would be created for url
        when(idFactoryImpl.generateAndGetIdentifier()).thenReturn("host-port-45689-3659-0");
        UrlResponse urlResponse = UrlServiceImpl.persistentUrlDetails("http://bit.ly/SaaYw5");
        assertEquals("http://bit.ly/SaaYw5", urlResponse.getUrl());
        assertEquals("http://short", urlResponse.getShortenedUrl());
    }

    @Test
    public void testPersistentUrlDetailsWhenApiServiceException() {
        when(shortenedUrlRepository.save(any())).thenReturn(getMockedUrlEntityObj());
        when(urlMapStructMapper.toUrlResponse(any())).thenReturn(getMockedUrlResponseObj());
        when(idFactoryImpl.generateAndGetIdentifier()).thenThrow(new ApiServiceException(API_SERVICE_EXCEPTION_OCCURRED));
        when(messageSource.getMessage(any(), any(), any())).thenReturn(API_SERVICE_EXCEPTION_OCCURRED);
        Throwable exception = assertThrows(ApiServiceException.class, () -> UrlServiceImpl.persistentUrlDetails("http://bit.ly/SaaYw5"));
        assertEquals(API_SERVICE_EXCEPTION_OCCURRED, exception.getMessage());
    }

    private UrlEntity getMockedUrlEntityObj() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrl("http://bit.ly/SaaYw5");
        urlEntity.setCreatedOn(new Date());
        urlEntity.setShortenedUrl("http://short");
        urlEntity.setTotalNumberOfHits(5);
        urlEntity.setLastAccessedOn(new Date());
        return urlEntity;
    }

    private UrlResponse getMockedUrlResponseObj() {
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setUrl("http://bit.ly/SaaYw5");
        urlResponse.setShortenedUrl("http://short");
        return urlResponse;
    }
}
