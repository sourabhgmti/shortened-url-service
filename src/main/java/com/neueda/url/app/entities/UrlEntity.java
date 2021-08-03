package com.neueda.url.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "URL_STATISTICS")
public class UrlEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @Column
    private long urlId;
    private String urlIdentifier;
    private String url;
    private String shortenedUrl;
    private long totalNumberOfHits;
    private Date createdOn;
    private Date lastAccessedOn;
}
