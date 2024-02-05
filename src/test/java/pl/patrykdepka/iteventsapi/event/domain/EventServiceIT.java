package pl.patrykdepka.iteventsapi.event.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.core.BaseIT;
import pl.patrykdepka.iteventsapi.event.domain.dto.CityDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createEventParticipant;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createUser;
import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

class EventServiceIT extends BaseIT {

    @Autowired
    private EventService eventService;

    @Test
    void shouldReturnFirst10EventsOrderedByDateTimeAsc() {
        // when
        List<EventItemListDTO> events = eventService.findFirst10UpcomingEvents();
        // then
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.size()).isEqualTo(10);
        events.forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnEventWithGivenId() {
        // given
        var eventId = 5L;
        var currentUser = createUser();
        // when
        var event = eventService.findEventById(eventId, currentUser);
        // then
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.getName()).isEqualTo("Java Dev Talks #5");
        assertThat(event.getImageType()).isEqualTo(EVENT_IMAGE);
        assertThat(event.getImageData()).isNotBlank();
        assertThat(event.getEventType()).isEqualTo(MEETING.getDisplayName());
        assertThat(event.getDate()).isEqualTo("15.05." + YEAR);
        assertThat(event.getHour()).isEqualTo("18:00");
        assertThat(event.getLanguage()).isEqualTo("polski");
        assertThat(event.getAdmission()).isEqualTo(FREE.getDisplayName());
        assertThat(event.getCity()).isEqualTo("Rzeszów");
        assertThat(event.getLocation()).isEqualTo("WSIiZ");
        assertThat(event.getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(event.getOrganizerId()).isEqualTo(2L);
        assertThat(event.getOrganizerImageType()).isEqualTo(PROFILE_IMAGE);
        assertThat(event.getOrganizerImageData()).isNotBlank();
        assertThat(event.getOrganizerName()).isEqualTo("Organizer Organizer");
        assertThat(event.getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
        assertThat(event.isCurrentUserIsParticipant()).isFalse();
    }

    @Test
    void shouldReturnAllCities() {
        // when
        List<CityDTO> cities = eventService.findAllCities();
        // then
        assertThat(cities.isEmpty()).isFalse();
        assertThat(cities.size()).isEqualTo(2);
        assertThat(
                cities.containsAll(
                        List.of(
                                new CityDTO("rzeszow", "Rzeszów"),
                                new CityDTO("warszawa", "Warszawa"))
                        )
        ).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnUpcomingEvents(int pageNumber) {
        // given
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> upcomingEvents = eventService.findUpcomingEvents(page);
        // then
        assertThat(upcomingEvents.getTotalPages()).isEqualTo(2);
        assertThat(upcomingEvents.getTotalElements()).isEqualTo(14);
        assertThat(upcomingEvents.getNumber()).isEqualTo(pageNumber);
        upcomingEvents.getContent().forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnUpcomingEventsByCity() {
        // given
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> upcomingEvents = eventService.findUpcomingEventsByCity(city, page);
        // then
        assertThat(upcomingEvents.getTotalPages()).isEqualTo(1);
        assertThat(upcomingEvents.getTotalElements()).isEqualTo(7);
        assertThat(upcomingEvents.getNumber()).isEqualTo(0);
        upcomingEvents.getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        upcomingEvents.getContent().forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnPastEvents() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> pastEvents = eventService.findPastEvents(page);
        // then
        assertThat(pastEvents.getTotalPages()).isEqualTo(1);
        assertThat(pastEvents.getTotalElements()).isEqualTo(10);
        assertThat(pastEvents.getNumber()).isEqualTo(0);
        pastEvents.getContent().forEach(e -> assertThat(e.getDate().isBefore(TODAY)).isTrue());
    }

    @Test
    void shouldReturnPastEventsByCity() {
        // given
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> pastEvents = eventService.findPastEventsByCity(city, page);
        // then
        assertThat(pastEvents.getTotalPages()).isEqualTo(1);
        assertThat(pastEvents.getTotalElements()).isEqualTo(5);
        assertThat(pastEvents.getNumber()).isEqualTo(0);
        pastEvents.getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        pastEvents.getContent().forEach(e -> assertThat(e.getDate().isBefore(TODAY)).isTrue());
    }

    @Test
    void shouldAddUserToEventParticipantList() {
        // given
        var eventId = 5L;
        var currentUser = createUser();
        // when
        var event = eventService.addUserToEventParticipantsList(eventId, currentUser);
        // then
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.isCurrentUserIsParticipant()).isTrue();
    }

    @Test
    void shouldRemoveUserFromEventParticipantList() {
        // given
        var eventId = 5L;
        var participant = createEventParticipant();
        // when
        var event = eventService.removeUserFromEventParticipantsList(eventId, participant);
        // then
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.isCurrentUserIsParticipant()).isFalse();
    }

    @Test
    void shouldReturnEventsWhereGivenUserIsParticipant() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> userEvents = eventService.findUserEvents(createEventParticipant(), page);
        // then
        assertThat(userEvents.getTotalPages()).isEqualTo(1);
        assertThat(userEvents.getTotalElements()).isEqualTo(6);
        assertThat(userEvents.getNumber()).isEqualTo(0);
    }

    @Test
    void shouldReturnEventsByCityWhereGivenUserIsParticipant() {
        // given
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> userEvents = eventService.findUserEventsByCity(createEventParticipant(), city, page);
        // then
        assertThat(userEvents.getTotalPages()).isEqualTo(1);
        assertThat(userEvents.getTotalElements()).isEqualTo(6);
        assertThat(userEvents.getNumber()).isEqualTo(0);
        userEvents.getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
    }
}