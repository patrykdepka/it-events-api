package pl.patrykdepka.iteventsapi.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.event.domain.OrganizerEventService;
import pl.patrykdepka.iteventsapi.event.domain.dto.*;
import pl.patrykdepka.iteventsapi.event.domain.exception.CityNotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class OrganizerEventController {
    private final OrganizerEventService organizerEventService;
    private final CurrentUserFacade currentUserFacade;

    @PostMapping("/organizer/events")
    ResponseEntity<EventDTO> createEvent(@Valid @RequestBody CreateEventDTO newEventData) {
        EventDTO event = organizerEventService.createEvent(currentUserFacade.getCurrentUser(), newEventData);
        URI eventUri = ServletUriComponentsBuilder
                .fromUriString("/api/v1/events")
                .path("/{id}")
                .buildAndExpand(event.getId())
                .toUri();
        return ResponseEntity.created(eventUri).body(event);
    }

    @GetMapping("/organizer/events")
    Page<EventCardDTO> findOrganizerEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return organizerEventService.findOrganizerEvents(currentUserFacade.getCurrentUser(), pageRequest);
    }

    @GetMapping("/organizer/events/cities/{city}")
    Page<EventCardDTO> findOrganizerEventsByCity(
            @PathVariable String city,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        List<CityDTO> cities = organizerEventService.findAllCities();
        city = getCity(cities, city);
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return organizerEventService.findOrganizerEventsByCity(currentUserFacade.getCurrentUser(), city, pageRequest);
    }

    @GetMapping("/organizer/events/{id}/edit")
    EventEditDTO showEditEventForm(@PathVariable Long id) {
        return organizerEventService.findEventToEdit(currentUserFacade.getCurrentUser(), id);
    }

    @PutMapping("/organizer/events/{id}")
    EventEditDTO updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventEditDTO eventEditData
    ) {
        return organizerEventService.updateEvent(currentUserFacade.getCurrentUser(), id, eventEditData);
    }

    @GetMapping("/organizer/events/{id}/participants")
    Page<ParticipantDTO> getEventParticipants(
            @PathVariable Long id,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        return organizerEventService.findEventParticipants(currentUserFacade.getCurrentUser(), id, pageRequest);
    }

    @PutMapping("/organizer/events/{eventId}/participants/{participantId}")
    Page<ParticipantDTO> removeParticipantFromEvent(
            @PathVariable Long eventId,
            @PathVariable Long participantId,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        return organizerEventService.removeParticipant(currentUserFacade.getCurrentUser(), eventId, participantId, pageRequest);
    }

    private String getCity(List<CityDTO> cities, String city) {
        for (CityDTO cityDTO : cities) {
            if (cityDTO.getNameWithoutPlCharacters().equals(city)) {
                return cityDTO.getDisplayName();
            }
        }

        throw new CityNotFoundException("City with name " + city + " not found");
    }
}
