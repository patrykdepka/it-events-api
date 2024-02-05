package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.event.domain.dto.CreateEventDTO;

import java.time.LocalDateTime;

import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;

public class CreateEventDTOCreator {

    public static CreateEventDTO create(LocalDateTime dateTime) {
        return CreateEventDTO.builder()
                .name("Dev Meets #001")
                .eventType(MEETING)
                .dateTime(dateTime.toString())
                .language("polski")
                .admission(FREE)
                .city("Rzeszów")
                .location("WSIiZ")
                .address("Sucharskiego 2, 35-225 Rzeszów")
                .description("Spotkanie rzeszowskich developerów.")
                .build();
    }
}
