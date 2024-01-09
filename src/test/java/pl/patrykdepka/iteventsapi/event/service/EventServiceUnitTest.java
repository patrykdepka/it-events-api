package pl.patrykdepka.iteventsapi.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.EventCreator;
import pl.patrykdepka.iteventsapi.event.domain.EventService;
import pl.patrykdepka.iteventsapi.event.domain.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.event.domain.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ORGANIZER;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_USER;

class EventServiceUnitTest {
    static final LocalDateTime DATE_TIME = LocalDateTime.now().withHour(18).withMinute(0);
    static final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "dateTime"));
    private EventRepository eventRepository;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepository = Mockito.mock(EventRepository.class);
        eventService = new EventService(eventRepository);
    }

    @Test
    void shouldReturnFirst10UpcomingEvents() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findFirst10EventsByOrderByDateTimeAsc()).thenReturn(events);
        // when
        List<EventCardDTO> returnedEvents = eventService.findFirst10UpcomingEvents();
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldReturnEvent() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        // when
        EventDTO returnedEvent = eventService.findEvent(event.getId(), user);
        // then
        assertThat(returnedEvent.getId()).isEqualTo(event.getId());
        assertThat(returnedEvent.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldReturnAllCities() {
        // given
        List<String> cities = List.of(
                "Rzeszów", "Kraków", "Warszawa"
        );
        when(eventRepository.findAllCities()).thenReturn(cities);
        // when
        List<CityDTO> returnedCities = eventService.findAllCities();
        // then
        assertThat(returnedCities).isNotEmpty();
        assertThat(returnedCities).hasSize(3);
    }

    @Test
    void shouldReturnAllUpcomingEvents() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findAllUpcomingEvents(DATE_TIME, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findAllUpcomingEvents(DATE_TIME, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldReturnUpcomingEventsByCity() {
        // given
        String city = "Rzeszów";
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findUpcomingEventsByCity(city, DATE_TIME, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findUpcomingEventsByCity(city, DATE_TIME, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldReturnAllPastEvents() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME, organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findAllPastEvents(DATE_TIME, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findAllPastEvents(DATE_TIME, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldReturnPastEventsByCity() {
        // given
        String city = "Rzeszów";
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME, organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findPastEventsByCity(city, DATE_TIME, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findPastEventsByCity(city, DATE_TIME, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldAddUserToEventParticipantsList() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski", ROLE_USER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        // when
        eventService.addUserToEventParticipantsList(user, event.getId());
        // then
        verify(eventRepository, Mockito.times(1)).findById(eq(event.getId()));
    }

    @Test
    void shouldRemoveUserFromEventParticipantsList() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        event.addParticipant(user);
        AppUser user2 = AppUserCreator.create(3L, "Patryk", "Kowalski");
        event.addParticipant(user2);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        // when
        eventService.removeUserFromEventParticipantsList(user2, event.getId());
        // then
        assertThat(event.getParticipants()).isNotEmpty();
        assertThat(event.getParticipants()).hasSize(1);
    }

    @Test
    void shouldGetUserEvents() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findUserEvents(organizer, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findUserEvents(organizer, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldGetUserEventsByCity() {
        // given
        String city = "Rzeszów";
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer),
                EventCreator.create(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #4", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(5L, "Java Dev Talks #5", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(6L, "Java Dev Talks #6", DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create(7L, "Java Dev Talks #7", DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create(8L, "Java Dev Talks #8", DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create(9L, "Java Dev Talks #9", DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create(10L, "Java Dev Talks #10", DATE_TIME.plusWeeks(9L), organizer)
        );
        when(eventRepository.findUserEventsByCity(organizer, city, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = eventService.findUserEventsByCity(organizer, city, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }
}
