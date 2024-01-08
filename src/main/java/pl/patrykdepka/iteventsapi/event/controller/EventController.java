package pl.patrykdepka.iteventsapi.event.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.event.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.exception.CityNotFoundException;
import pl.patrykdepka.iteventsapi.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private final EventService eventService;
    private final CurrentUserFacade currentUserFacade;

    public EventController(EventService eventService, CurrentUserFacade currentUserFacade) {
        this.eventService = eventService;
        this.currentUserFacade = currentUserFacade;
    }

    @GetMapping("/home")
    public List<EventCardDTO> showMainPage() {
        return eventService.findFirst10UpcomingEvents();
    }

    @GetMapping("/events/{id}")
    public EventDTO getEvent(@PathVariable Long id) {
        return eventService.findEvent(id, currentUserFacade.getCurrentUser());
    }

    @GetMapping("/events")
    public Page<EventCardDTO> getAllUpcomingEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        return eventService.findAllUpcomingEvents(LocalDateTime.now(), pageRequest);
    }

    @GetMapping("/events/cities/{city}")
    public Page<EventCardDTO> getUpcomingEventsByCity(@PathVariable String city,
                                                      @RequestParam(name = "page", required = false) Integer pageNumber) {
        List<CityDTO> cities = eventService.findAllCities();
        city = getCity(cities, city);
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        return eventService.findUpcomingEventsByCity(city, LocalDateTime.now(), pageRequest);
    }

    @GetMapping("/archive/events")
    public Page<EventCardDTO> getAllPastEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findAllPastEvents(LocalDateTime.now(), pageRequest);
    }

    @GetMapping("/archive/events/cities/{city}")
    public Page<EventCardDTO> getPastEventsByCity(@PathVariable String city,
                                                  @RequestParam(name = "page", required = false) Integer pageNumber) {
        List<CityDTO> cities = eventService.findAllCities();
        city = getCity(cities, city);
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findPastEventsByCity(city, LocalDateTime.now(), pageRequest);
    }

    @PostMapping("/events/{id}/join")
    public ResponseEntity<EventDTO> joinEvent(@PathVariable Long id) {
        EventDTO event = eventService.addUserToEventParticipantsList(currentUserFacade.getCurrentUser(), id);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/events/{id}/leave")
    public ResponseEntity<EventDTO> leaveEvent(@PathVariable Long id) {
        EventDTO event = eventService.removeUserFromEventParticipantsList(currentUserFacade.getCurrentUser(), id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/events/my_events")
    public Page<EventCardDTO> getUserEvents(@RequestParam(name = "page", required = false) Integer pageNumber) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findUserEvents(currentUserFacade.getCurrentUser(), pageRequest);
    }

    @GetMapping("/events/my_events/cities/{city}")
    public Page<EventCardDTO> getUserEventsByCity(@PathVariable String city,
                                                  @RequestParam(name = "page", required = false) Integer pageNumber) {
        List<CityDTO> cities = eventService.findAllCities();
        city = getCity(cities, city);
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        return eventService.findUserEventsByCity(currentUserFacade.getCurrentUser(), city, pageRequest);
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
