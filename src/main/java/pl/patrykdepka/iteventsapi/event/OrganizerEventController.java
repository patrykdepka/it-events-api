package pl.patrykdepka.iteventsapi.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.event.domain.OrganizerEventService;
import pl.patrykdepka.iteventsapi.event.domain.dto.CreateEventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.ParticipantDTO;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.data.domain.Sort.Direction.ASC;

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
    Page<EventItemListDTO> getOrganizerEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(ASC, "dateTime"));
        return organizerEventService.findOrganizerEvents(currentUserFacade.getCurrentUser(), pageRequest);
    }

    @GetMapping("/organizer/events/cities/{city}")
    Page<EventItemListDTO> getOrganizerEventsByCity(
            @PathVariable String city,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(ASC, "dateTime"));
        return organizerEventService.findOrganizerEventsByCity(currentUserFacade.getCurrentUser(), city, pageRequest);
    }

    @GetMapping("/organizer/events/{id}/edit")
    EventEditDTO getEventToEdit(@PathVariable Long id) {
        return organizerEventService.findEventToEdit(currentUserFacade.getCurrentUser(), id);
    }

    @PutMapping("/organizer/events/{id}")
    EventEditDTO updateEvent(@PathVariable Long id, @Valid @RequestBody EventEditDTO eventEditData) {
        return organizerEventService.updateEvent(currentUserFacade.getCurrentUser(), id, eventEditData);
    }

    @GetMapping("/organizer/events/{id}/participants")
    Page<ParticipantDTO> getEventParticipants(
            @PathVariable Long id,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(ASC, "lastName"));
        return organizerEventService.findEventParticipants(currentUserFacade.getCurrentUser(), id, pageRequest);
    }

    @PutMapping("/organizer/events/{id}/participants/{participantId}")
    Page<ParticipantDTO> removeParticipantFromEvent(
            @PathVariable Long id,
            @PathVariable Long participantId,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(ASC, "lastName"));
        return organizerEventService.removeParticipant(currentUserFacade.getCurrentUser(), id, participantId, pageRequest);
    }
}
