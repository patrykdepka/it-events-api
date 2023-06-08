package pl.patrykdepka.iteventsapi.event.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.event.dto.*;

import java.util.List;

public interface OrganizerEventService {

    EventDTO createEvent(AppUser currentUser, CreateEventDTO newEventData);

    List<CityDTO> findAllCities();

    Page<EventCardDTO> findOrganizerEvents(AppUser currentUser, Pageable page);

    Page<EventCardDTO> findOrganizerEventsByCity(AppUser currentUser, String city, Pageable page);

    EventEditDTO findEventToEdit(AppUser currentUser, Long id);

    EventEditDTO updateEvent(AppUser currentUser, Long id, EventEditDTO EventEditDTO);

    Page<ParticipantDTO> findEventParticipants(AppUser currentUser, Long id, Pageable page);

    Page<ParticipantDTO> removeParticipant(AppUser currentUser, Long eventId, Long participantId, Pageable page);
}
