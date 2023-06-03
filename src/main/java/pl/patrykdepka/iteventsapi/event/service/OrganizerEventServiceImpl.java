package pl.patrykdepka.iteventsapi.event.service;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.event.dto.*;
import pl.patrykdepka.iteventsapi.event.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.mapper.EventCardDTOMapper;
import pl.patrykdepka.iteventsapi.event.mapper.EventDTOMapper;
import pl.patrykdepka.iteventsapi.event.mapper.EventEditDTOMapper;
import pl.patrykdepka.iteventsapi.event.mapper.ParticipantDTOMapper;
import pl.patrykdepka.iteventsapi.event.model.Event;
import pl.patrykdepka.iteventsapi.event.repository.EventRepository;
import pl.patrykdepka.iteventsapi.eventimage.service.EventImageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizerEventServiceImpl implements OrganizerEventService {
    private final EventRepository eventRepository;
    private final EventImageService eventImageService;

    public OrganizerEventServiceImpl(EventRepository eventRepository, EventImageService eventImageService) {
        this.eventRepository = eventRepository;
        this.eventImageService = eventImageService;
    }

    public EventDTO createEvent(AppUser currentUser, CreateEventDTO newEventData) {
        Event event = new Event();
        event.setName(newEventData.getName());
        event.setEventImage(eventImageService.createDefaultEventImage());
        event.setEventType(newEventData.getEventType());
        event.setDateTime(LocalDateTime.parse(newEventData.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.setLanguage(newEventData.getLanguage());
        event.setAdmission(newEventData.getAdmission());
        event.setCity(newEventData.getCity());
        event.setLocation(newEventData.getLocation());
        event.setAddress(newEventData.getAddress());
        event.setOrganizer(currentUser);
        event.setDescription(newEventData.getDescription());
        return EventDTOMapper.mapToEventDTO(eventRepository.save(event));
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

    public Page<EventCardDTO> findOrganizerEvents(AppUser currentUser, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findOrganizerEvents(currentUser, page));
    }

    public Page<EventCardDTO> findOrganizerEventsByCity(AppUser currentUser, String city, Pageable page) {
        return EventCardDTOMapper.mapToEventCardDTOs(eventRepository.findOrganizerEventsByCity(currentUser, city, page));
    }

    public EventEditDTO findEventToEdit(AppUser currentUser, Long id) {
        return EventEditDTOMapper.mapToEventEditDTO(returnEventIfCurrentUserIsOrganizer(currentUser, id));
    }

    @Transactional
    public void updateEvent(AppUser currentUser, EventEditDTO editEventDTO) {
        eventRepository
                .findById(editEventDTO.getId())
                .ifPresentOrElse(
                        event -> {
                            setEventFields(editEventDTO, event);
                        },
                        () -> {
                            throw new EventNotFoundException("Event with ID " + editEventDTO.getId() + " not found");
                        });
    }

    public Page<ParticipantDTO> findEventParticipants(AppUser currentUser, Long id, Pageable page) {
        Event event = returnEventIfCurrentUserIsOrganizer(currentUser, id);
        return ParticipantDTOMapper.mapToParticipantDTOs(event.getParticipants(), page);
    }

    @Transactional
    public void removeParticipant(AppUser currentUser, Long eventId, Long participantId) {
        Event event = returnEventIfCurrentUserIsOrganizer(currentUser, eventId);
        AppUser user = event.getParticipants().stream().filter(participant -> participant.getId() == participantId).findFirst().get();
        event.removeParticipant(user);
    }

    private String getCityNameWithoutPlCharacters(String city) {
        city = city.toLowerCase();
        city = city.replace("\\s", "-");
        city = StringUtils.stripAccents(city);
        return city;
    }

    private Event returnEventIfCurrentUserIsOrganizer(AppUser currentUser, Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (!currentUser.equals(event.getOrganizer())) {
                throw new AccessDeniedException("Access is denied");
            }

            return event;
        }

        throw new EventNotFoundException("Event with ID " + id + " not found");
    }

    private Event setEventFields(EventEditDTO source, Event target) {
        if (source.getName() != null && !source.getName().equals(target.getName())) {
            target.setName(source.getName());
        }
        if (!source.getEventImage().isEmpty()) {
            eventImageService.updateEventImage(target, source.getEventImage()).ifPresent(target::setEventImage);
        }
        if (source.getDateTime() != null && !source.getDateTime().equals(target.getDateTime().toString())) {
            target.setDateTime(LocalDateTime.parse(source.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (source.getEventType() != null && source.getEventType() != target.getEventType()) {
            target.setEventType(source.getEventType());
        }
        if (source.getLanguage() != null && !source.getLanguage().equals(target.getLanguage())) {
            target.setLanguage(source.getLanguage());
        }
        if (source.getAdmission() != null && source.getAdmission() != target.getAdmission()) {
            target.setAdmission(source.getAdmission());
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }
        if (source.getLocation() != null && !source.getLocation().equals(target.getLocation())) {
            target.setLocation(source.getLocation());
        }
        if (source.getAddress() != null && !source.getAddress().equals(target.getAddress())) {
            target.setAddress(source.getAddress());
        }
        if (source.getDescription() != null && !source.getDescription().equals(target.getDescription())) {
            target.setDescription(source.getDescription());
        }

        return target;
    }
}
