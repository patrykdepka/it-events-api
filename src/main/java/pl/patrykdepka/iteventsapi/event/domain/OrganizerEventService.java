package pl.patrykdepka.iteventsapi.event.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.dictionary.domain.DictCity;
import pl.patrykdepka.iteventsapi.dictionary.domain.DictCityService;
import pl.patrykdepka.iteventsapi.event.domain.dto.CreateEventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.ParticipantDTO;
import pl.patrykdepka.iteventsapi.event.domain.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.domain.mapper.ParticipantDTOMapper;
import pl.patrykdepka.iteventsapi.image.domain.ImageService;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.apache.commons.lang3.StringUtils.stripAccents;
import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventDTOMapper.mapToEventDTO;
import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventEditDTOMapper.mapToEventEditDTO;
import static pl.patrykdepka.iteventsapi.event.domain.mapper.EventItemListDTOMapper.mapToEventItemListDTOs;
import static pl.patrykdepka.iteventsapi.image.domain.ImageService.DEFAULT_EVENT_IMAGE_NAME;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;

@Service
@RequiredArgsConstructor
public class OrganizerEventService {
    private final Logger logger = LoggerFactory.getLogger(OrganizerEventService.class);
    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final DictCityService dictCityService;
    private final AppUserRepository appUserRepository;

    public EventDTO createEvent(AppUser currentUser, CreateEventDTO newEventData) {
        Event event = new Event();
        event.setName(newEventData.getName());
        event.setEventImage(imageService.createDefaultImage(DEFAULT_EVENT_IMAGE_NAME, EVENT_IMAGE));
        event.setEventType(newEventData.getEventType());
        event.setDateTime(LocalDateTime.parse(newEventData.getDateTime(), ISO_LOCAL_DATE_TIME));
        event.setLanguage(newEventData.getLanguage());
        event.setAdmission(newEventData.getAdmission());
        event.setCity(getCity(newEventData.getCity()));
        event.setLocation(newEventData.getLocation());
        event.setAddress(newEventData.getAddress());
        event.setOrganizer(currentUser);
        event.setDescription(newEventData.getDescription());
        Event createdEvent = eventRepository.save(event);
        logger.info("Event [ID: {}] was created by user [ID: {}]", createdEvent.getId(), currentUser.getId());
        return mapToEventDTO(createdEvent, currentUser);
    }

    @Transactional
    public Page<EventItemListDTO> findOrganizerEvents(AppUser currentUser, Pageable page) {
        return mapToEventItemListDTOs(eventRepository.findOrganizerEvents(currentUser, page));
    }

    @Transactional
    public Page<EventItemListDTO> findOrganizerEventsByCity(AppUser currentUser, String cityUrnName, Pageable page) {
        String city = dictCityService.findCityByUrnName(cityUrnName).getDisplayName();
        return mapToEventItemListDTOs(eventRepository.findOrganizerEventsByCity(currentUser, city, page));
    }

    public EventEditDTO findEventToEdit(AppUser currentUser, Long eventId) {
        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + eventId + "] not found"));
        if (!event.getOrganizer().equals(currentUser)) {
            throw new AccessDeniedException("Access is denied");
        }
        return mapToEventEditDTO(event);
    }

    public EventEditDTO updateEvent(AppUser currentUser, Long id, EventEditDTO eventEditData) {
        Event event = eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
        if (!event.getOrganizer().equals(currentUser)) {
            throw new AccessDeniedException("Access is denied");
        }
        setEventFields(eventEditData, event);
        logger.info("Event [ID: {}] was updated by user [ID: {}]", event.getId(), currentUser.getId());
        return mapToEventEditDTO(event);
    }

    public Page<ParticipantDTO> findEventParticipants(AppUser currentUser, Long id, Pageable page) {
        Event event = eventRepository.findEventAndItsParticipantsByEventId(id)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + id + "] not found"));
        if (!currentUser.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Access is denied");
        }
        List<Long> participants = event.getParticipants().stream()
                .map(EventsAppUsers::getAppUserId)
                .toList();
        return ParticipantDTOMapper.mapToParticipantDTOs(appUserRepository.findAllById(participants), page);
    }

    @Transactional
    public Page<ParticipantDTO> removeParticipant(AppUser currentUser, Long eventId, Long participantId, Pageable page) {
        Event event = eventRepository.findEventAndItsParticipantsByEventId(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event [ID: " + eventId + "] not found"));
        if (!currentUser.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Access is denied");
        }
        if (event.checkIfUserIsParticipant(participantId)) {
            event.removeParticipant(participantId);
            logger.info("User [ID: {}] was removed from event [ID: {}] participants list by user [ID: {}]", participantId, eventId, currentUser.getId());
        }
        List<Long> participants = event.getParticipants().stream()
                .map(EventsAppUsers::getAppUserId)
                .toList();
        return ParticipantDTOMapper.mapToParticipantDTOs(appUserRepository.findAllById(participants), page);
    }

    private String getCity(String cityDisplayName) {
        String cityUrnName = prepareCityUrnName(cityDisplayName);
        return dictCityService.findCityByUrnNameIfExists(cityUrnName)
                .orElse(new DictCity(cityUrnName, cityDisplayName.trim()))
                .getDisplayName();
    }

    private String prepareCityUrnName(String city) {
        String urnName = city.trim();
        urnName = urnName.toLowerCase();
        urnName = urnName.replace("\\s", "-");
        urnName = stripAccents(urnName);
        return urnName;
    }

    private void setEventFields(EventEditDTO source, Event target) {
        if (source.getName() != null && !source.getName().equals(target.getName())) {
            target.setName(source.getName());
        }
        if (source.getEventImage() != null) {
            imageService.updateImage(target.getEventImage().getId(), source.getEventImage());
        }
        if (source.getDateTime() != null && !source.getDateTime().equals(target.getDateTime().toString())) {
            target.setDateTime(LocalDateTime.parse(source.getDateTime(), ISO_LOCAL_DATE_TIME));
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
            target.setCity(getCity(source.getCity()));
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
    }
}
