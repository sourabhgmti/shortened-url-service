package com.neueda.url.app.validation;


import com.neueda.url.app.exception.BadUrlException;
import com.neueda.url.app.message.ServiceMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final MessageSource messageSource;

    /**
     *
     * @param url
     */
    public void validateRequestUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new BadUrlException(messageSource.getMessage(ServiceMessage.ValidationMessageSourceCode.URL_NOT_EMPTY, null, Locale.ENGLISH));
        }
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new BadUrlException(messageSource.getMessage(ServiceMessage.ValidationMessageSourceCode.URL_NOT_VALID, null, Locale.ENGLISH));
        }
    }
}
