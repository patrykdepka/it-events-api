package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
