package com.neueda.url.app.mapstruct;

import com.neueda.url.app.entities.UrlEntity;
import com.neueda.url.app.model.UrlResponse;
import com.neueda.url.app.model.UrlStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UrlMapStructMapper {
    UrlResponse toUrlResponse(UrlEntity urlEntity);

    UrlEntity toUrlEntity(UrlResponse urlResponse);

    UrlStatistics toUrlStatistics(UrlEntity urlEntity);
}
