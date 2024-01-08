package pl.patrykdepka.iteventsapi.event.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.event.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventCardDTO> findFirst10UpcomingEvents();

    EventDTO findEvent(Long id, AppUser currentUser);

    List<CityDTO> findAllCities();

    Page<EventCardDTO> findAllUpcomingEvents(LocalDateTime currentDateTime, Pageable page);

    Page<EventCardDTO> findUpcomingEventsByCity(String city, LocalDateTime currentDateTime, Pageable page);

    Page<EventCardDTO> findAllPastEvents(LocalDateTime currentDateTime, Pageable page);

    Page<EventCardDTO> findPastEventsByCity(String city, LocalDateTime currentDateTime, Pageable page);

    EventDTO addUserToEventParticipantsList(AppUser currentUser, Long id);

    EventDTO removeUserFromEventParticipantsList(AppUser currentUser, Long id);

    Page<EventCardDTO> findUserEvents(AppUser currentUser, Pageable page);

    Page<EventCardDTO> findUserEventsByCity(AppUser currentUser, String city, Pageable page);
}
