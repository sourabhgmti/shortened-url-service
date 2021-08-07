package com.neueda.url.app;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {ShortenedUrlApplication.class})
@ActiveProfiles("test")
public class AbstractTest {
}
