package pl.patrykdepka.iteventsapi.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;
import pl.patrykdepka.iteventsapi.core.TestPageImpl;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

class EventControllerIT extends BaseControllerIT {

    @Test
    void shouldReturnFirst10UpcomingEvents() {
        // when
        ResponseEntity<List<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/main-page",
                GET,
                sendRequestAsUnauthorizedUser(null),
                new ParameterizedTypeReference<>() {
                }
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().size()).isEqualTo(10);
        response.getBody().forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnEventWithGivenId() {
        // given
        var eventId = 5;
        // when
        ResponseEntity<EventDTO> response = restTemplate.exchange(
                "/api/v1/events/" + eventId,
                GET,
                sendRequestAsUser(null),
                EventDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(eventId);
        assertThat(response.getBody().getName()).isEqualTo("Java Dev Talks #5");
        assertThat(response.getBody().getImageType()).isEqualTo(EVENT_IMAGE);
        assertThat(response.getBody().getImageData()).isNotBlank();
        assertThat(response.getBody().getEventType()).isEqualTo(MEETING.getDisplayName());
        assertThat(response.getBody().getDate()).isEqualTo("15.05." + YEAR);
        assertThat(response.getBody().getHour()).isEqualTo("18:00");
        assertThat(response.getBody().getLanguage()).isEqualTo("polski");
        assertThat(response.getBody().getAdmission()).isEqualTo(FREE.getDisplayName());
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getLocation()).isEqualTo("WSIiZ");
        assertThat(response.getBody().getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(response.getBody().getOrganizerId()).isEqualTo(2L);
        assertThat(response.getBody().getOrganizerImageType()).isEqualTo(PROFILE_IMAGE);
        assertThat(response.getBody().getOrganizerImageData()).isNotBlank();
        assertThat(response.getBody().getOrganizerName()).isEqualTo("Organizer Organizer");
        assertThat(response.getBody().getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
        assertThat(response.getBody().isCurrentUserIsParticipant()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnUpcomingEvents(int pageNumber) {
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/events?page=" + pageNumber,
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(2);
        assertThat(response.getBody().getTotalElements()).isEqualTo(14);
        assertThat(response.getBody().getNumber()).isEqualTo(pageNumber - 1);
        response.getBody().getContent().forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnUpcomingEventsByCity() {
        // given
        var city = "Rzeszów";
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/events/cities/rzeszow",
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(7);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        response.getBody().getContent().forEach(e -> assertThat(e.getDate().isAfter(TODAY)).isTrue());
    }

    @Test
    void shouldReturnPastEvents() {
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/archive/events",
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(10);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().getContent().forEach(e -> assertThat(e.getDate().isBefore(TODAY)).isTrue());
    }

    @Test
    void shouldReturnPastEventsByCity() {
        // given
        var city = "Rzeszów";
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/archive/events/cities/rzeszow",
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(5);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
        response.getBody().getContent().forEach(e -> assertThat(e.getDate().isBefore(TODAY)).isTrue());
    }

    @Test
    void shouldAddUserToEventParticipantList() {
        // given
        var eventId = 5;
        // when
        ResponseEntity<EventDTO> response = restTemplate.exchange(
                "/api/v1/events/" + eventId + "/join",
                POST,
                sendRequestAsUser(null),
                EventDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(eventId);
        assertThat(response.getBody().isCurrentUserIsParticipant()).isTrue();
    }

    @Test
    void shouldRemoveUserFromEventParticipantList() {
        // given
        var eventId = 5;
        // when
        ResponseEntity<EventDTO> response = restTemplate.exchange(
                "/api/v1/events/" + eventId + "/leave",
                POST,
                sendRequestAsEventParticipant(null),
                EventDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(eventId);
        assertThat(response.getBody().isCurrentUserIsParticipant()).isFalse();
    }

    @Test
    void shouldReturnEventsWhereUserIsParticipant() {
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/events/my",
                GET,
                sendRequestAsEventParticipant(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(6);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
    }

    @Test
    void shouldReturnEventsByCityWhereUserIsParticipant() {
        // when
        var city = "Rzeszów";
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/events/my/cities/rzeszow",
                GET,
                sendRequestAsEventParticipant(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(6);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
    }
}
