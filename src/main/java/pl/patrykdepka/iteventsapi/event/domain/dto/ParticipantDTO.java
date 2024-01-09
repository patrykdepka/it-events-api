package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.Value;

@Value
public class ParticipantDTO {
    Long id;
    String firstName;
    String lastName;
}
