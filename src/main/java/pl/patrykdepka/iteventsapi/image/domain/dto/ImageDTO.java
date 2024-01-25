package pl.patrykdepka.iteventsapi.image.domain.dto;

import lombok.Value;

@Value
public class ImageDTO {
    String filename;
    String content;
    String contentType;
}
