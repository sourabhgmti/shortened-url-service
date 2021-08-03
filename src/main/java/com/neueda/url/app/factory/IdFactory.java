package com.neueda.url.app.factory;

import org.springframework.stereotype.Service;

@Service
public interface IdFactory {

    String generateAndGetIdentifier();
}
