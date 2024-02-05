package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.PAID;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.ONLINE;

public class EventEditDTOCreator {

    public static EventEditDTO create(LocalDateTime dateTime) {
        return EventEditDTO.builder()
                .name("Testowa nazwa")
                .eventImageData(null)
                .eventImage(null)
                .eventType(ONLINE)
                .dateTime(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .language("Testowy język")
                .admission(PAID)
                .city("Testowe miasto")
                .location("Testowa lokacja")
                .address("Testowy adres")
                .description("Testowy opis")
                .build();
    }
}
