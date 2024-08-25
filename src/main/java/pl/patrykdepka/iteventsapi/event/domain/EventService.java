package pl.patrykdepka.iteventsapi.event.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.core.DateTimeProvider;
import pl.patrykdepka.iteventsapi.dictionary.domain.DictCityService;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.domain.mapper.EventItemListDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventDTOMapper.mapToEventDTO;
import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventItemListDTOMapper.mapToEventItemListDTOs;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final DateTimeProvider dateTimeProvider;
    private final DictCityService dictCityService;

    public List<EventItemListDTO> findFirst10UpcomingEvents() {
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        List<Event> upcomingEvents = eventRepository.findFirst10UpcomingEvents(dateTimeProvider.getCurrentDateTime(), page).getContent();
        return EventItemListDTOMapper.mapToEventItemListDTOs(upcomingEvents);
    }

    public EventDTO findEventById(Long id, AppUser currentUser) {
        return eventRepository.findEventById(id)
                .map(event -> mapToEventDTO(event, currentUser))
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
    }

    public Page<EventItemListDTO> findUpcomingEvents(Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findUpcomingEvents(dateTimeProvider.getCurrentDateTime(), page));
    }

    public Page<EventItemListDTO> findUpcomingEventsByCity(String cityUrnName, Pageable page) {
        String city = dictCityService.findCityByUrnName(cityUrnName).getDisplayName();
        return mapToEventItemListDTOs(eventRepository.findUpcomingEventsByCity(dateTimeProvider.getCurrentDateTime(), city, page));
    }

    public Page<EventItemListDTO> findPastEvents(Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findPastEvents(dateTimeProvider.getCurrentDateTime(), page));
    }

    public Page<EventItemListDTO> findPastEventsByCity(String cityUrnName, Pageable page) {
        String city = dictCityService.findCityByUrnName(cityUrnName).getDisplayName();
        return mapToEventItemListDTOs(eventRepository.findPastEventsByCity(dateTimeProvider.getCurrentDateTime(), city, page));
    }

    public EventDTO addUserToEventParticipantsList(Long id, AppUser currentUser) {
        Event event = eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
        if (!event.checkIfUserIsParticipant(currentUser.getId())) {
            event.addParticipant(currentUser.getId());
            logger.info("User [ID: {}] was added to event [ID: {}] participants list", currentUser.getId(), event.getId());
        }

        return mapToEventDTO(event, currentUser);
    }

    public EventDTO removeUserFromEventParticipantsList(Long id, AppUser currentUser) {
        Event event = eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
        if (event.checkIfUserIsParticipant(currentUser.getId())) {
            event.removeParticipant(currentUser.getId());
            logger.info("User [ID: {}] was removed from event [ID: {}] participants list", currentUser.getId(), event.getId());
        }

        return mapToEventDTO(event, currentUser);
    }

    public Page<EventItemListDTO> findUserEvents(AppUser user, Pageable page) {
        List<EventItemListDTO> events = eventRepository.findEventsAndItsParticipants().stream()
                .filter(e -> e.checkIfUserIsParticipant(user.getId()))
                .map(EventItemListDTOMapper::mapToEventItemListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(events, page, events.size());
    }

    public Page<EventItemListDTO> findUserEventsByCity(AppUser user, String cityUrnName, Pageable page) {
        String city = dictCityService.findCityByUrnName(cityUrnName).getDisplayName();
        List<EventItemListDTO> events = eventRepository.findEventsAndItsParticipantsByCity(city).stream()
                .filter(e -> e.checkIfUserIsParticipant(user.getId()))
                .map(EventItemListDTOMapper::mapToEventItemListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(events, page, events.size());
    }
}
