package pl.patrykdepka.iteventsapi.event.domain.mapper;

import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.AdmissionType;
import pl.patrykdepka.iteventsapi.event.domain.EventType;
import pl.patrykdepka.iteventsapi.event.domain.Event;

import java.util.Base64;

public class EventEditDTOMapper {

    public static EventEditDTO mapToEventEditDTO(Event event) {
        EventEditDTO eventData = new EventEditDTO();
        eventData.setName(event.getName());
        eventData.setImageType(event.getEventImage().getFileType());
        eventData.setImageData(Base64.getEncoder().encodeToString(event.getEventImage().getFileData()));
        eventData.setEventType(EventType.valueOf(event.getEventType().toString()));
        eventData.setDateTime(event.getDateTime().toString());
        eventData.setLanguage(event.getLanguage());
        eventData.setAdmission(AdmissionType.valueOf(event.getAdmission().toString()));
        eventData.setCity(event.getCity());
        eventData.setLocation(event.getLocation());
        eventData.setAddress(event.getAddress());
        eventData.setDescription(event.getDescription());
        return eventData;
    }
}
