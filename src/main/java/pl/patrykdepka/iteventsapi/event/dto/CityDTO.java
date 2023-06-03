package pl.patrykdepka.iteventsapi.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityDTO {
    private String nameWithoutPlCharacters;
    private String displayName;
}
