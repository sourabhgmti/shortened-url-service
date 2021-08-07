package com.neueda.url.app.factory.impl;

import com.neueda.url.app.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IdFactoryImplTest extends AbstractTest {

    @Autowired
    private IdFactoryImpl idFactoryImpl;

    @Test
    public void generateAndGetIdentifierTest() {
        String uniqueUrlIdentifier = idFactoryImpl.generateAndGetIdentifier();
        Assertions.assertNotNull(uniqueUrlIdentifier);
    }
}
