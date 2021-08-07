package com.neueda.url.app.service.impl;

import com.neueda.url.app.entities.UrlEntity;
import com.neueda.url.app.exception.ApiServiceException;
import com.neueda.url.app.exception.UrlNotFoundException;
import com.neueda.url.app.factory.IdFactory;
import com.neueda.url.app.mapstruct.UrlMapStructMapper;
import com.neueda.url.app.message.ServiceMessage;
import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.model.UrlStatistics;
import com.neueda.url.app.repository.ShortenedUrlRepository;
import com.neueda.url.app.service.UrlService;
import com.neueda.url.app.validation.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UrlServiceImpl implements UrlService {

    private final ShortenedUrlRepository shortenedUrlRepository;
    private final IdFactory idFactoryImpl;
    private final RequestValidator requestValidator;
    private final UrlMapStructMapper urlMapStructMapper;
    private final MessageSource messageSource;

    @Value("${server.hostname}")
    private String hostName;

    @Value("${server.servlet.context-path}" + "/url")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 
     * @param url
     * @return
     */
    @Override
    public UrlResponse persistentUrlDetails(String url) {
        requestValidator.validateRequestUrl(url);
        log.debug("Validation for {} requested url is done.", url);
        try {
            UrlEntity urlEntityObj = Optional.ofNullable(shortenedUrlRepository.getShortenedUrlByUrl(url)).orElseGet(() -> {
                log.debug("Requested url {} entry is not found in database", url);
                UrlEntity urlEntity = populateUrlEntity(url);
                log.debug("Url entity is populated to save into database with id:{}, urlIdentifier:{}, url:{}," +
                                " shortenedUrl:{}, createdOn:{},lastAccessedOn:{}", urlEntity.getUrlId(), urlEntity.getUrlIdentifier(), urlEntity.getUrl(),
                        urlEntity.getShortenedUrl(), urlEntity.getCreatedOn(), urlEntity.getLastAccessedOn());
                return shortenedUrlRepository.save(urlEntity);
            });
            return urlMapStructMapper.toUrlResponse(urlEntityObj);
        } catch (Exception exception) {
            log.debug("Unable to save url entity into database due to: {}", exception.getMessage());
            throw new ApiServiceException(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_SERVICE_EXCEPTION, null, Locale.ENGLISH));
        }
    }

    /**
     *
     * @param urlIdentifier
     * @return
     */
    @Override
    public UrlResponse getUrlResponseByUrlIdentifier(String urlIdentifier) {
        return Optional.ofNullable(updateAndGetUrlStatistics(urlIdentifier))
                .orElseThrow(() -> new UrlNotFoundException(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_NOT_FOUND_EXCEPTION, null, Locale.ENGLISH)));
    }

    /**
     *
     * @param urlIdentifier
     * @return
     */
    private UrlResponse updateAndGetUrlStatistics(String urlIdentifier) {
        UrlEntity urlEntity = shortenedUrlRepository.getUrlEntityByUrlIdentifier(urlIdentifier);
        long hitCount = urlEntity.getTotalNumberOfHits();
        urlEntity.setTotalNumberOfHits(++hitCount);
        urlEntity.setLastAccessedOn(new Date());
        log.debug("Evaluated url statistics Hit Count: {}", hitCount);
        return urlMapStructMapper.toUrlResponse(shortenedUrlRepository.save(urlEntity));
    }

    /**
     *
     * @param urlIdentifier
     * @return
     */
    @Override
    public UrlStatistics getUrlStatisticsByUrlIdentifier(String urlIdentifier) {
        return Optional.ofNullable(
                        urlMapStructMapper.toUrlStatistics(shortenedUrlRepository.getUrlEntityByUrlIdentifier(urlIdentifier)))
                .orElseThrow(() -> new UrlNotFoundException(messageSource.getMessage(ServiceMessage.BusinessMessageSourceCode.URL_NOT_FOUND_EXCEPTION, null, Locale.ENGLISH)));
    }

    /**
     *
     * @param url
     * @return
     */
    private UrlEntity populateUrlEntity(String url) {
        UrlEntity urlEntity = new UrlEntity();
        String urlIdentifier = idFactoryImpl.generateAndGetIdentifier();
        urlEntity.setUrl(url);
        urlEntity.setShortenedUrl(String.format("%s:%s%s/%s", hostName, serverPort, contextPath, urlIdentifier));
        urlEntity.setUrlIdentifier(urlIdentifier);
        urlEntity.setCreatedOn(new Date());
        log.debug("Url entity is populated with all properties having identifier as: {}", urlIdentifier);
        return urlEntity;
    }
}
