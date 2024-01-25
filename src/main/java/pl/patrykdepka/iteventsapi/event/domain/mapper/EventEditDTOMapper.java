package pl.patrykdepka.iteventsapi.event.domain.mapper;

import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.AdmissionType;
import pl.patrykdepka.iteventsapi.event.domain.EventType;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import java.util.Base64;

public class EventEditDTOMapper {

    public static EventEditDTO mapToEventEditDTO(Event event) {
        return EventEditDTO.builder()
                .name(event.getName())
                .eventImageData(convertImageToBase64String(event.getEventImage()))
                .eventType(EventType.valueOf(event.getEventType().toString()))
                .dateTime(event.getDateTime().toString())
                .language(event.getLanguage())
                .admission(AdmissionType.valueOf(event.getAdmission().toString()))
                .city(event.getCity())
                .location(event.getLocation())
                .address(event.getAddress())
                .description(event.getDescription())
                .build();
    }

    private static String convertImageToBase64String(Image image) {
        return "data:" + image.getContentType() + ";base64," + Base64.getEncoder().encodeToString(image.getFileData());
    }
}
