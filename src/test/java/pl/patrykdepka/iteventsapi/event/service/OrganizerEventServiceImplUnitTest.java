package pl.patrykdepka.iteventsapi.event.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.creator.*;
import pl.patrykdepka.iteventsapi.event.dto.*;
import pl.patrykdepka.iteventsapi.event.exception.EventNotFoundException;
import pl.patrykdepka.iteventsapi.event.model.Event;
import pl.patrykdepka.iteventsapi.event.repository.EventRepository;
import pl.patrykdepka.iteventsapi.eventimage.service.EventImageService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static pl.patrykdepka.iteventsapi.appuser.model.Role.ROLE_ORGANIZER;

class OrganizerEventServiceImplUnitTest {
    static final LocalDateTime DATE_TIME = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).withHour(18).withMinute(0);
    static final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "dateTime"));
    private EventRepository eventRepository;
    private EventImageService eventImageService;
    private OrganizerEventServiceImpl organizerEventServiceImpl;

    @BeforeEach
    void setUp() {
        eventRepository = Mockito.mock(EventRepository.class);
        eventImageService = Mockito.mock(EventImageService.class);
        organizerEventServiceImpl = new OrganizerEventServiceImpl(eventRepository, eventImageService);
    }

    @Test
    void shouldReturnCreatedEvent() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        CreateEventDTO newEventData = CreateEventDTOCreator.create(DATE_TIME);
        when(eventImageService.createDefaultEventImage()).thenReturn(EventImageCreator.createDefaultEventImage(1L));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArguments()[0]);
        // when
        EventDTO returnedCreatedEvent = organizerEventServiceImpl.createEvent(organizer, newEventData);
        // then
        verify(eventRepository, times(1)).save(argThat((Event savedEvent) -> {
            Assertions.assertAll("Testing saved event",
                    () -> assertNull(savedEvent.getId()),
                    () -> assertEquals(newEventData.getName(), savedEvent.getName()),
                    () -> assertNotNull(savedEvent.getEventImage()),
                    () -> assertEquals(newEventData.getEventType(), savedEvent.getEventType()),
                    () -> assertEquals(newEventData.getDateTime(), savedEvent.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                    () -> assertEquals(newEventData.getLanguage(), savedEvent.getLanguage()),
                    () -> assertEquals(newEventData.getAdmission(), savedEvent.getAdmission()),
                    () -> assertEquals(newEventData.getCity(), savedEvent.getCity()),
                    () -> assertEquals(newEventData.getLocation(), savedEvent.getLocation()),
                    () -> assertEquals(newEventData.getAddress(), savedEvent.getAddress()),
                    () -> assertEquals(organizer, savedEvent.getOrganizer()),
                    () -> assertEquals(newEventData.getDescription(), savedEvent.getDescription()),
                    () -> assertThat(savedEvent.getParticipants()).isEmpty()
            );
            return true;
        }));
    }

    @Test
    void shouldReturnAllCities() {
        // given
        List<String> cities = List.of(
                "Rzeszów", "Kraków", "Warszawa"
        );
        when(eventRepository.findAllCities()).thenReturn(cities);
        // when
        List<CityDTO> returnedCities = organizerEventServiceImpl.findAllCities();
        // then
        assertThat(returnedCities).isNotEmpty();
        assertThat(returnedCities).hasSize(3);
    }

    @Test
    void shouldReturnOrganizerEvents() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #10", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(2L, "Java Dev Talks #9", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(3L, "Java Dev Talks #8", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #7", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(5L, "Java Dev Talks #6", DATE_TIME, organizer),
                EventCreator.create(6L, "Java Dev Talks #5", DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create(7L, "Java Dev Talks #4", DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create(8L, "Java Dev Talks #3", DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create(9L, "Java Dev Talks #2", DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create(10L, "Java Dev Talks #1", DATE_TIME.minusWeeks(5L), organizer)
        );
        when(eventRepository.findOrganizerEvents(organizer, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = organizerEventServiceImpl.findOrganizerEvents(organizer, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldReturnOrganizerEventsByCity() {
        // given
        String city = "Rzeszów";
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        List<Event> events = List.of(
                EventCreator.create(1L, "Java Dev Talks #10", DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create(2L, "Java Dev Talks #9", DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create(3L, "Java Dev Talks #8", DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create(4L, "Java Dev Talks #7", DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create(5L, "Java Dev Talks #6", DATE_TIME, organizer),
                EventCreator.create(6L, "Java Dev Talks #5", DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create(7L, "Java Dev Talks #4", DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create(8L, "Java Dev Talks #3", DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create(9L, "Java Dev Talks #2", DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create(10L, "Java Dev Talks #1", DATE_TIME.minusWeeks(5L), organizer)
        );
        when(eventRepository.findOrganizerEventsByCity(organizer, city, pageRequest)).thenReturn(new PageImpl<>(events, pageRequest, events.size()));
        // when
        Page<EventCardDTO> returnedEvents = organizerEventServiceImpl.findOrganizerEventsByCity(organizer, city, pageRequest);
        // then
        assertThat(returnedEvents).isNotEmpty();
        assertThat(returnedEvents).hasSize(10);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotEventOrganizer() {
        // given
        AppUser user = AppUserCreator.create(5L, "Patryk", "Nowak", ROLE_ORGANIZER);
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        // then
        assertThatThrownBy(() -> organizerEventServiceImpl.findEventToEdit(user, event.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }

    @Test
    void shouldReturnEventToEdit() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        // when
        EventEditDTO returnedEventToEdit = organizerEventServiceImpl.findEventToEdit(organizer, event.getId());
        // then
        assertThat(returnedEventToEdit).isNotNull();
        assertThat(returnedEventToEdit.getName()).isEqualTo(event.getName());
        assertThat(returnedEventToEdit).isNotNull();
        assertThat(returnedEventToEdit.getImageType()).isEqualTo(event.getEventImage().getFileType());
        assertThat(returnedEventToEdit.getImageData()).isEqualTo(Base64.getEncoder().encodeToString(event.getEventImage().getFileData()));
        assertThat(returnedEventToEdit.getEventType()).isEqualTo(event.getEventType());
        assertThat(returnedEventToEdit.getDateTime()).isEqualTo(event.getDateTime().toString());
        assertThat(returnedEventToEdit.getLanguage()).isEqualTo(event.getLanguage());
        assertThat(returnedEventToEdit.getAdmission()).isEqualTo(event.getAdmission());
        assertThat(returnedEventToEdit.getCity()).isEqualTo(event.getCity());
        assertThat(returnedEventToEdit.getLocation()).isEqualTo(event.getLocation());
        assertThat(returnedEventToEdit.getAddress()).isEqualTo(event.getAddress());
        assertThat(returnedEventToEdit.getDescription()).isEqualTo(event.getDescription());
    }

    @Test
    void shouldThrowExceptionIfEventNotFound() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> organizerEventServiceImpl.findEventToEdit(organizer, event.getId()))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage(String.format("Event with ID %s not found", event.getId()));
    }

    @Test
    void shouldUpdateEvent() throws IOException {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        EventEditDTO newEventData = EventEditDTOCreator.create(DATE_TIME);
        // when
        organizerEventServiceImpl.updateEvent(organizer, event.getId(), newEventData);
        // then
        verify(eventRepository, times(1)).findById(eq(event.getId()));
    }

    @Test
    void shouldReturnEventParticipants() {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        event.addParticipant(AppUserCreator.create(2L, "Jan", "Kowalski"));
        event.addParticipant(AppUserCreator.create(3L, "Patryk", "Kowalski"));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        // when
        Page<ParticipantDTO> participants = organizerEventServiceImpl.findEventParticipants(organizer, event.getId(), pageRequest);
        // then
        assertThat(participants).isNotEmpty();
        assertThat(participants).hasSize(2);
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
        organizerEventServiceImpl.removeParticipant(organizer, event.getId(), user2.getId(), pageRequest);
        // then
        assertThat(event.getParticipants()).isNotEmpty();
        assertThat(event.getParticipants()).hasSize(1);
    }
}
