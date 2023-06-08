package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.event.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.enumeration.AdmissionType;
import pl.patrykdepka.iteventsapi.event.enumeration.EventType;

import java.io.IOException;
import java.time.LocalDateTime;

public class EventEditDTOCreator {

    public static EventEditDTO create(LocalDateTime dateTime) throws IOException {
        return EventEditDTO.builder()
                .name("Updated test name")
//                .eventImage(EventImageCreator.createNewEventImageFile())
                .eventType(EventType.CONFERENCE)
                .dateTime(dateTime.plusWeeks(1L).toString())
                .language("Updated test language")
                .admission(AdmissionType.PAID)
                .city("Updated test city")
                .location("Updated test location")
                .address("Updated test address")
                .description("Updated test description")
                .build();
    }
}
