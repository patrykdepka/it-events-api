package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.Value;

@Value
public class CityDTO {
    String nameWithoutPlCharacters;
    String displayName;
}
