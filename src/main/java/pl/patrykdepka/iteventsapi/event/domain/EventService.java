package pl.patrykdepka.iteventsapi.event.domain;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.event.domain.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.domain.mapper.EventCardDTOMapper;
import pl.patrykdepka.iteventsapi.event.domain.mapper.EventDTOMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventCardDTO> findFirst10UpcomingEvents() {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findFirst10EventsByOrderByDateTimeAsc());
    }

    public EventDTO findEvent(Long id, AppUser currentUser) {
        return eventRepository
                .findById(id)
                .map(event -> EventDTOMapper.mapToEventDTO(event, currentUser))
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " not found"));
    }

    public List<CityDTO> findAllCities() {
        List<String> cities = eventRepository.findAllCities();
        List<CityDTO> cityDTOs = new ArrayList<>();
        for (String city : cities) {
            CityDTO cityDTO = new CityDTO();
            cityDTO.setNameWithoutPlCharacters(getCityNameWithoutPlCharacters(city));
            cityDTO.setDisplayName(city);
            cityDTOs.add(cityDTO);
        }
        return cityDTOs;
    }

    public Page<EventCardDTO> findAllUpcomingEvents(LocalDateTime currentDateTime, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findAllUpcomingEvents(currentDateTime, page));
    }

    public Page<EventCardDTO> findUpcomingEventsByCity(String city, LocalDateTime currentDateTime, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findUpcomingEventsByCity(city, currentDateTime, page));
    }

    public Page<EventCardDTO> findAllPastEvents(LocalDateTime currentDateTime, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findAllPastEvents(currentDateTime, page));
    }

    public Page<EventCardDTO> findPastEventsByCity(String city, LocalDateTime currentDateTime, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findPastEventsByCity(city, currentDateTime, page));
    }

    @Transactional
    public EventDTO addUserToEventParticipantsList(AppUser currentUser, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            if (!event.checkIfUserIsParticipant(currentUser)) {
                event.addParticipant(currentUser);
                logger.info("User [ID: " + currentUser.getId() + "] added to event [ID: " + event.getId() + "] participants list");
            }

            return EventDTOMapper.mapToEventDTO(event, currentUser);
        }

        throw new EventNotFoundException("Event with ID " + id + " not found");
    }

    @Transactional
    public EventDTO removeUserFromEventParticipantsList(AppUser currentUser, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            if (event.checkIfUserIsParticipant(currentUser)) {
                event.removeParticipant(currentUser);
                logger.info("User [ID: " + currentUser.getId() + "] removed from event [ID: " + event.getId() + "] participants list");
            }

            return EventDTOMapper.mapToEventDTO(event, currentUser);
        }

        throw new EventNotFoundException("Event with ID " + id + " not found");
    }

    public Page<EventCardDTO> findUserEvents(AppUser user, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findUserEvents(user, page));
    }

    public Page<EventCardDTO> findUserEventsByCity(AppUser user, String city, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findUserEventsByCity(user, city, page));
    }

    private String getCityNameWithoutPlCharacters(String city) {
        city = city.toLowerCase();
        city = city.replace("\\s", "-");
        city = StringUtils.stripAccents(city);
        return city;
    }
}
