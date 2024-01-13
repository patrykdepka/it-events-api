package pl.patrykdepka.iteventsapi.event.domain.mapper;

import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.AdmissionType;
import pl.patrykdepka.iteventsapi.event.domain.EventType;
import pl.patrykdepka.iteventsapi.event.domain.Event;

import java.util.Base64;

public class EventEditDTOMapper {

    public static EventEditDTO mapToEventEditDTO(Event event) {

        return new EventEditDTO(
                event.getName(),
                event.getEventImage().getType(),
                Base64.getEncoder().encodeToString(event.getEventImage().getFileData()),
                null,
                EventType.valueOf(event.getEventType().toString()),
                event.getDateTime().toString(),
                event.getLanguage(),
                AdmissionType.valueOf(event.getAdmission().toString()),
                event.getCity(),
                event.getLocation(),
                event.getAddress(),
                event.getDescription()
        );
    }
}
