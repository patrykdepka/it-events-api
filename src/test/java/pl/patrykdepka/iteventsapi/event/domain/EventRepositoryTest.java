package pl.patrykdepka.iteventsapi.event.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createOrganizer;
import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;

class EventRepositoryTest extends BaseIT {

    @Test
    void shouldReturnFirst10EventsOrderedByDateTimeAsc() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findFirst10UpcomingEvents(NOW, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(2);
        assertThat(events.getTotalElements()).isEqualTo(14);
        assertThat(events.getNumber()).isEqualTo(0);
        events.forEach(e -> assertThat(e.getDateTime().isAfter(NOW)).isTrue());
    }

    @Test
    void shouldReturnEventWithGivenId() {
        // given
        var eventId = 5L;
        // when
        Optional<Event> event = eventRepository.findEventById(eventId);
        // then
        assertThat(event).isNotEmpty();
        assertThat(event.get().getId()).isEqualTo(eventId);
        assertThat(event.get().getName()).isEqualTo("Java Dev Talks #5");
        assertThat(event.get().getEventImage()).isNotNull();
        assertThat(event.get().getEventType()).isEqualTo(MEETING);
        assertThat(event.get().getDateTime()).isEqualTo(LocalDateTime.of(YEAR, 5, 15, 18, 0));
        assertThat(event.get().getLanguage()).isEqualTo("polski");
        assertThat(event.get().getAdmission()).isEqualTo(FREE);
        assertThat(event.get().getCity()).isEqualTo("Rzeszów");
        assertThat(event.get().getLocation()).isEqualTo("WSIiZ");
        assertThat(event.get().getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(event.get().getOrganizer()).isEqualTo(createOrganizer());
        assertThat(event.get().getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnUpcomingEvents(int pageNumber) {
        // given
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findUpcomingEvents(NOW, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(2);
        assertThat(events.getTotalElements()).isEqualTo(14);
        assertThat(events.getNumber()).isEqualTo(pageNumber);
        events.forEach(e -> assertThat(e.getDateTime().isAfter(NOW)).isTrue());
    }

    @Test
    void shouldReturnUpcomingEventsByCity() {
        // given
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findUpcomingEventsByCity(NOW, city, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(1);
        assertThat(events.getTotalElements()).isEqualTo(7);
        assertThat(events.getNumber()).isEqualTo(0);
        events.forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        events.forEach(e -> assertThat(e.getDateTime().isAfter(NOW)).isTrue());
    }

    @Test
    void shouldReturnPastEvents() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findPastEvents(NOW, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(1);
        assertThat(events.getTotalElements()).isEqualTo(10);
        assertThat(events.getNumber()).isEqualTo(0);
        events.forEach(e -> assertThat(e.getDateTime().isBefore(NOW)).isTrue());
    }

    @Test
    void shouldReturnPastEventsByCity() {
        // given
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findPastEventsByCity(NOW, city, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(1);
        assertThat(events.getTotalElements()).isEqualTo(5);
        assertThat(events.getNumber()).isEqualTo(0);
        events.forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        events.forEach(e -> assertThat(e.getDateTime().isBefore(NOW)).isTrue());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void shouldReturnOrganizerEvents(int pageNumber) {
        // given
        var organizer = createOrganizer();
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findOrganizerEvents(organizer, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(3);
        assertThat(events.getTotalElements()).isEqualTo(24);
        assertThat(events.getNumber()).isEqualTo(pageNumber);
        events.forEach(e -> assertThat(e.getOrganizer()).isEqualTo(organizer));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnOrganizerEventsByCity(int pageNumber) {
        // given
        var organizer = createOrganizer();
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "dateTime"));
        // when
        Page<Event> events = eventRepository.findOrganizerEventsByCity(organizer, city, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(2);
        assertThat(events.getTotalElements()).isEqualTo(12);
        assertThat(events.getNumber()).isEqualTo(pageNumber);
        events.forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        events.forEach(e -> assertThat(e.getOrganizer()).isEqualTo(organizer));
    }

    @Test
    void shouldReturnEventAndItsParticipantsByEventId() {
        // given
        var eventId = 5L;
        // when
        Optional<Event> event = eventRepository.findEventAndItsParticipantsByEventId(eventId);
        // then
        assertThat(event).isNotEmpty();
        assertThat(event.get().getId()).isEqualTo(eventId);
        assertThat(event.get().getName()).isEqualTo("Java Dev Talks #5");
        assertThat(event.get().getEventImage()).isNotNull();
        assertThat(event.get().getEventType()).isEqualTo(MEETING);
        assertThat(event.get().getDateTime()).isEqualTo(LocalDateTime.of(YEAR, 5, 15, 18, 0));
        assertThat(event.get().getLanguage()).isEqualTo("polski");
        assertThat(event.get().getAdmission()).isEqualTo(FREE);
        assertThat(event.get().getCity()).isEqualTo("Rzeszów");
        assertThat(event.get().getLocation()).isEqualTo("WSIiZ");
        assertThat(event.get().getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(event.get().getOrganizer()).isEqualTo(createOrganizer());
        assertThat(event.get().getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
        assertThat(event.get().getParticipants().isEmpty()).isFalse();
    }

    @Test
    void shouldReturnEventsAndItsParticipants() {
        // when
        List<Event> events = eventRepository.findEventsAndItsParticipants();
        // then
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.size()).isEqualTo(24);
        events.forEach(e -> assertThat(e.getParticipants().isEmpty() || !e.getParticipants().isEmpty()).isTrue());
    }

    @Test
    void shouldReturnEventsAndItsParticipantsByCity() {
        // given
        var city = "Rzeszów";
        // when
        List<Event> events = eventRepository.findEventsAndItsParticipantsByCity(city);
        // then
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.size()).isEqualTo(12);
        events.forEach(e -> assertThat(e.getCity()).isEqualTo(city));
        events.forEach(e -> assertThat(e.getParticipants().isEmpty() || !e.getParticipants().isEmpty()).isTrue());
    }
}
