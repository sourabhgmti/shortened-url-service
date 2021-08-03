package com.neueda.url.app.repository;

import com.neueda.url.app.entities.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<UrlEntity, Long> {
    UrlEntity getShortenedUrlByUrl(String url);

    UrlEntity getUrlEntityByUrlIdentifier(String urlIdentifier);
}
