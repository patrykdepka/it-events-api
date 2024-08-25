package pl.patrykdepka.iteventsapi.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.event.domain.EventService;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class EventController {
    private final EventService eventService;
    private final CurrentUserFacade currentUserFacade;

    @GetMapping("/main-page")
    List<EventItemListDTO> getAllDataForMainPage() {
        return eventService.findFirst10UpcomingEvents();
    }

    @GetMapping("/events/{id}")
    EventDTO getEventById(@PathVariable Long id) {
        return eventService.findEventById(id, currentUserFacade.getCurrentUser());
    }

    @GetMapping("/events")
    Page<EventItemListDTO> getUpcomingEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        return eventService.findUpcomingEvents(pageRequest);
    }

    @GetMapping("/events/cities/{city}")
    Page<EventItemListDTO> getUpcomingEventsByCity(
            @PathVariable String city,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        return eventService.findUpcomingEventsByCity(city, pageRequest);
    }

    @GetMapping("/archive/events")
    Page<EventItemListDTO> getPastEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findPastEvents(pageRequest);
    }

    @GetMapping("/archive/events/cities/{city}")
    Page<EventItemListDTO> getPastEventsByCity(
            @PathVariable String city,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findPastEventsByCity(city, pageRequest);
    }

    @PostMapping("/events/{id}/join")
    EventDTO joinEvent(@PathVariable Long id) {
        return eventService.addUserToEventParticipantsList(id, currentUserFacade.getCurrentUser());
    }

    @PostMapping("/events/{id}/leave")
    EventDTO leaveEvent(@PathVariable Long id) {
        return eventService.removeUserFromEventParticipantsList(id, currentUserFacade.getCurrentUser());
    }

    @GetMapping("/events/my")
    Page<EventItemListDTO> getUserEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findUserEvents(currentUserFacade.getCurrentUser(), pageRequest);
    }

    @GetMapping("/events/my/cities/{city}")
    Page<EventItemListDTO> getUserEventsByCity(
            @PathVariable String city,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findUserEventsByCity(currentUserFacade.getCurrentUser(), city, pageRequest);
    }
}
