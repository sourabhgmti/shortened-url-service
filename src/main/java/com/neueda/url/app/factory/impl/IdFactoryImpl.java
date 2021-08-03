package com.neueda.url.app.factory.impl;

import com.neueda.url.app.factory.IdFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class IdFactoryImpl implements IdFactory {

    private final String hostName;
    private final long creationTimeMillis;
    private long lastTimeMillis;
    private long discriminator;

    public IdFactoryImpl() throws UnknownHostException {
        this.hostName = InetAddress.getLocalHost().getHostName();
        this.creationTimeMillis = System.currentTimeMillis();
        this.lastTimeMillis = creationTimeMillis;
    }

    @Override
    public String generateAndGetIdentifier() {
        String initialReference = StringUtils.EMPTY;
        AtomicReference<String> uniqueUrlIdentifier = new AtomicReference(initialReference);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis == lastTimeMillis) {
            ++discriminator;
        } else {
            discriminator = 0;
        }
        String newReference = String.format("%s-%d-%d-%d", hostName, creationTimeMillis, currentTimeMillis, discriminator);
        lastTimeMillis = currentTimeMillis;
        boolean exchanged = uniqueUrlIdentifier.compareAndSet(initialReference, newReference);
        log.debug("Url identifier isExchanged? : {} ",exchanged);
        return uniqueUrlIdentifier.get();
    }

}
