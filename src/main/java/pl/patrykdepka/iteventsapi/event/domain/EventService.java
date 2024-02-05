package pl.patrykdepka.iteventsapi.event.domain;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
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
import pl.patrykdepka.iteventsapi.event.domain.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.domain.mapper.EventItemListDTOMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventDTOMapper.mapToEventDTO;
import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventItemListDTOMapper.mapToEventItemListDTOs;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final DateTimeProvider dateTimeProvider;

    public List<EventItemListDTO> findFirst10UpcomingEvents() {
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        List<Event> upcomingEvents = eventRepository.findFirst10UpcomingEvents(dateTimeProvider.getCurrentDateTime(), page).getContent();
        return EventItemListDTOMapper.mapToEventItemListDTOs(upcomingEvents);
    }

    @Transactional
    public EventDTO findEventById(Long id, AppUser currentUser) {
        return eventRepository.findById(id)
                .map(event -> mapToEventDTO(event, currentUser))
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
    }

    public List<CityDTO> findAllCities() {
        List<String> cities = eventRepository.findAllCities();
        List<CityDTO> cityDTOs = new ArrayList<>();
        for (String city : cities) {
            CityDTO cityDTO = new CityDTO(
                    getCityNameWithoutPlCharacters(city),
                    city
            );
            cityDTOs.add(cityDTO);
        }
        return cityDTOs;
    }

    public Page<EventItemListDTO> findUpcomingEvents(Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findUpcomingEvents(dateTimeProvider.getCurrentDateTime(), page));
    }

    public Page<EventItemListDTO> findUpcomingEventsByCity(String city, Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findUpcomingEventsByCity(dateTimeProvider.getCurrentDateTime(), city, page));
    }

    public Page<EventItemListDTO> findPastEvents(Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findPastEvents(dateTimeProvider.getCurrentDateTime(), page));
    }

    public Page<EventItemListDTO> findPastEventsByCity(String city, Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findPastEventsByCity(dateTimeProvider.getCurrentDateTime(), city, page));
    }

    @Transactional
    public EventDTO addUserToEventParticipantsList(Long id, AppUser currentUser) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
        if (!event.checkIfUserIsParticipant(currentUser.getId())) {
            event.addParticipant(currentUser.getId());
            logger.info("User [ID: {}] was added to event [ID: {}] participants list", currentUser.getId(), event.getId());
        }

        return mapToEventDTO(event, currentUser);
    }

    @Transactional
    public EventDTO removeUserFromEventParticipantsList(Long id, AppUser currentUser) {
        Event event = eventRepository.findById(id)
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

    public Page<EventItemListDTO> findUserEventsByCity(AppUser user, String city, Pageable page) {
        List<EventItemListDTO> events = eventRepository.findEventsAndItsParticipantsByCity(city).stream()
                .filter(e -> e.checkIfUserIsParticipant(user.getId()))
                .map(EventItemListDTOMapper::mapToEventItemListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(events, page, events.size());
    }

    private String getCityNameWithoutPlCharacters(String city) {
        city = city.toLowerCase();
        city = city.replace("\\s", "-");
        city = StringUtils.stripAccents(city);
        return city;
    }
}
