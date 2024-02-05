package pl.patrykdepka.iteventsapi.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;
import pl.patrykdepka.iteventsapi.core.TestPageImpl;
import pl.patrykdepka.iteventsapi.creator.CreateEventDTOCreator;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventEditDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.ParticipantDTO;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createEventParticipant;
import static pl.patrykdepka.iteventsapi.creator.EventEditDTOCreator.create;
import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

class OrganizerEventControllerIT extends BaseControllerIT {

    @Test
    void shouldCreateEvent() {
        // given
        var newEventData = CreateEventDTOCreator.create(NOW.plusMonths(1));
        // when
        ResponseEntity<EventDTO> response = restTemplate.exchange(
                "/api/v1/organizer/events",
                POST,
                sendRequestAsOrganizer(newEventData),
                EventDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Dev Meets #001");
        assertThat(response.getBody().getImageType()).isEqualTo(EVENT_IMAGE);
        assertThat(response.getBody().getImageData()).isNotBlank();
        assertThat(response.getBody().getEventType()).isEqualTo(MEETING.getDisplayName());
        assertThat(response.getBody().getDate()).isEqualTo(NOW.plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(response.getBody().getHour()).isEqualTo(NOW.plusMonths(1).format(DateTimeFormatter.ofPattern("HH:mm")));
        assertThat(response.getBody().getLanguage()).isEqualTo(newEventData.getLanguage());
        assertThat(response.getBody().getAdmission()).isEqualTo(FREE.getDisplayName());
        assertThat(response.getBody().getCity()).isEqualTo(newEventData.getCity());
        assertThat(response.getBody().getLocation()).isEqualTo(newEventData.getLocation());
        assertThat(response.getBody().getAddress()).isEqualTo(newEventData.getAddress());
        assertThat(response.getBody().getOrganizerId()).isEqualTo(2L);
        assertThat(response.getBody().getOrganizerImageType()).isEqualTo(PROFILE_IMAGE);
        assertThat(response.getBody().getOrganizerImageData()).isNotNull();
        assertThat(response.getBody().getOrganizerName()).isEqualTo("Organizer Organizer");
        assertThat(response.getBody().getDescription()).isEqualTo(newEventData.getDescription());
        assertThat(response.getBody().isCurrentUserIsParticipant()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void shouldReturnEventsWhereUserIsOrganizer(int pageNumber) {
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/organizer/events?page=" + pageNumber,
                GET,
                sendRequestAsOrganizer(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(3);
        assertThat(response.getBody().getTotalElements()).isEqualTo(24);
        assertThat(response.getBody().getNumber()).isEqualTo(pageNumber - 1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnEventsByCityWhereUserIsOrganizer(int pageNumber) {
        // given
        var city = "Rzeszów";
        // when
        ResponseEntity<TestPageImpl<EventItemListDTO>> response = restTemplate.exchange(
                "/api/v1/organizer/events/cities/rzeszow?page=" + pageNumber,
                GET,
                sendRequestAsOrganizer(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(2);
        assertThat(response.getBody().getTotalElements()).isEqualTo(12);
        assertThat(response.getBody().getNumber()).isEqualTo(pageNumber - 1);
        response.getBody().getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
    }

    @Test
    void shouldReturnEventToEdit() {
        // when
        ResponseEntity<EventEditDTO> response = restTemplate.exchange(
                "/api/v1/organizer/events/6/edit",
                GET,
                sendRequestAsOrganizer(null),
                EventEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Java Dev Talks #6");
        assertThat(response.getBody().getEventImageData()).isNotBlank();
        assertThat(response.getBody().getEventImage()).isNull();
        assertThat(response.getBody().getEventType()).isEqualTo(MEETING);
        assertThat(response.getBody().getDateTime()).isEqualTo(NOW.plusWeeks(2).format(ISO_LOCAL_DATE_TIME));
        assertThat(response.getBody().getLanguage()).isEqualTo("polski");
        assertThat(response.getBody().getAdmission()).isEqualTo(FREE);
        assertThat(response.getBody().getCity()).isEqualTo("Rzeszów");
        assertThat(response.getBody().getLocation()).isEqualTo("WSIiZ");
        assertThat(response.getBody().getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(response.getBody().getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedEventWithGivenId() {
        // given
        var newEventData = create(NOW.plusWeeks(2));
        // when
        ResponseEntity<EventEditDTO> response = restTemplate.exchange(
                "/api/v1/organizer/events/6",
                PUT,
                sendRequestAsOrganizer(newEventData),
                EventEditDTO.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(newEventData.getName());
        assertThat(response.getBody().getEventImageData()).isNotBlank();
        assertThat(response.getBody().getEventImage()).isNull();
        assertThat(response.getBody().getEventType()).isEqualTo(newEventData.getEventType());
        assertThat(response.getBody().getDateTime()).isEqualTo(newEventData.getDateTime());
        assertThat(response.getBody().getLanguage()).isEqualTo(newEventData.getLanguage());
        assertThat(response.getBody().getAdmission()).isEqualTo(newEventData.getAdmission());
        assertThat(response.getBody().getCity()).isEqualTo(newEventData.getCity());
        assertThat(response.getBody().getLocation()).isEqualTo(newEventData.getLocation());
        assertThat(response.getBody().getAddress()).isEqualTo(newEventData.getAddress());
        assertThat(response.getBody().getDescription()).isEqualTo(newEventData.getDescription());
    }

    @Test
    void shouldReturnEventParticipants() {
        // when
        ResponseEntity<TestPageImpl<ParticipantDTO>> response = restTemplate.exchange(
                "/api/v1/organizer/events/5/participants",
                GET,
                sendRequestAsOrganizer(null),
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
    void shouldRemoveUserFromEventParticipantsAndReturnParticipantList() {
        // given
        var eventParticipant = createEventParticipant();
        // when
        ResponseEntity<TestPageImpl<ParticipantDTO>> response = restTemplate.exchange(
                "/api/v1/organizer/events/5/participants/5",
                PUT,
                sendRequestAsOrganizer(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
        assertThat(response.getBody().getTotalElements()).isEqualTo(5);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        response.getBody().getContent().forEach(p -> assertThat(p.getId().equals(eventParticipant.getId())).isFalse());
    }
}
