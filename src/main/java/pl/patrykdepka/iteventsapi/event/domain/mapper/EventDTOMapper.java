package pl.patrykdepka.iteventsapi.event.domain.mapper;

import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class EventDTOMapper {

    public static EventDTO mapToEventDTO(Event event, AppUser user) {
        return new EventDTO.EventDTOBuilder()
                .id(event.getId())
                .name(event.getName())
                .imageType(event.getEventImage().getType())
                .imageData(Base64.getEncoder().encodeToString(event.getEventImage().getFileData()))
                .eventType(event.getEventType().getDisplayName())
                .date(event.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .hour(event.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .language(event.getLanguage())
                .admission(event.getAdmission().getDisplayName())
                .city(event.getCity())
                .location(event.getLocation())
                .address(event.getAddress())
                .organizerId(event.getOrganizer().getId())
                .organizerImageType(event.getOrganizer().getProfileImage().getType())
                .organizerImageData(Base64.getEncoder().encodeToString(event.getOrganizer().getProfileImage().getFileData()))
                .organizerName(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName())
                .description(event.getDescription())
                .currentUserIsParticipant(event.checkIfUserIsParticipant(user.getId()))
                .build();
    }
}
