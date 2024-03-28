package org.java.scrapper.converter;

import java.net.URI;
import java.util.List;
import org.java.scrapper.dto.link.LinkResponse;
import org.java.scrapper.model.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface LinkConverter {
    @Mapping(target = "url", source = "url", qualifiedByName = "toURI")
    LinkResponse toDto(Link link);

    @Mapping(target = "url", source = "url", qualifiedByName = "toURI")
    List<LinkResponse> toDto(List<Link> links);

    @Named("toURI")
    default URI toURI(String url) {
        return URI.create(url);
    }
}
