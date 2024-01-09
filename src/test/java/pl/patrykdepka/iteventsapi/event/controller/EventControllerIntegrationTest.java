package pl.patrykdepka.iteventsapi.event.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.EventCreator;
import pl.patrykdepka.iteventsapi.creator.EventImageCreator;
import pl.patrykdepka.iteventsapi.creator.ProfileImageCreator;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventDTO;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.event.domain.EventRepository;
import pl.patrykdepka.iteventsapi.eventimage.repository.EventImageRepository;
import pl.patrykdepka.iteventsapi.profileimage.repository.ProfileImageRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ORGANIZER;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {
    static final LocalDateTime DATE_TIME = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).withHour(18).withMinute(0);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ProfileImageRepository profileImageRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventImageRepository eventImageRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void shouldReturnFirst10UpcomingEvents() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        appUserRepository.save(organizer);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(7L), organizer),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(8L), organizer),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(9L), organizer)
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/home");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        List<EventCardDTO> returnedEvents = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<EventCardDTO>>() {});
        assertThat(returnedEvents.size()).isEqualTo(10);
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnEvent() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        List<AppUser> users = List.of(
                organizer,
                user
        );
        appUserRepository.saveAll(users);
        Event event = EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer);
        eventRepository.save(event);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/events/" + event.getId());
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        EventDTO returnedEvent = objectMapper.readValue(result.getResponse().getContentAsString(), EventDTO.class);
        assertThat(returnedEvent.getId()).isEqualTo(event.getId());
    }

    @Test
    @Transactional
    void shouldReturnAllUpcomingEvents() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        appUserRepository.save(organizer);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer)
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/events");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(7)));
    }

    @Test
    @Transactional
    void shouldReturnUpcomingEventsByCity() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        appUserRepository.save(organizer);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer)
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/events/cities/rzeszow");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(7)));
    }

    @Test
    @Transactional
    void shouldReturnAllPastEvents() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        appUserRepository.save(organizer);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer)
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/archive/events");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(6)));
    }

    @Test
    @Transactional
    void shouldReturnPastEventsByCity() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        appUserRepository.save(organizer);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer)
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/archive/events/cities/rzeszow");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(6)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldAddUserToEventParticipantsList() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        List<AppUser> users = List.of(
                organizer,
                user
        );
        appUserRepository.saveAll(users);
        Event event = EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer);
        eventRepository.save(event);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/events/" + event.getId() + "/join");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        EventDTO returnedEvent = objectMapper.readValue(result.getResponse().getContentAsString(), EventDTO.class);
        assertThat(returnedEvent.isCurrentUserIsParticipant()).isEqualTo(true);
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldRemoveUserFromEventParticipantsList() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        List<AppUser> users = List.of(
                organizer,
                user
        );
        appUserRepository.saveAll(users);
        Event event = EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer);
        event.addParticipant(user);
        eventRepository.save(event);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/events/" + event.getId() + "/leave");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        EventDTO returnedEvent = objectMapper.readValue(result.getResponse().getContentAsString(), EventDTO.class);
        assertThat(returnedEvent.isCurrentUserIsParticipant()).isEqualTo(false);
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldGetUserEvents() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        List<AppUser> users = List.of(
                organizer,
                user
        );
        appUserRepository.saveAll(users);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer, List.of(user))
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/events/my_events");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldGetUserEventsByCity() throws Exception {
        // given
        AppUser organizer = AppUserCreator.create("Jan", "Nowak", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", profileImageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER);
        List<AppUser> users = List.of(
                organizer,
                user
        );
        appUserRepository.saveAll(users);
        List<Event> events = List.of(
                EventCreator.create("Java Dev Talks #1", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(6L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #2", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(5L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #3", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(4L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #4", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(3L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #5", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(2L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #6", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.minusWeeks(1L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #7", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME, organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #8", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(1L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #9", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(2L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #10", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(3L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #11", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(4L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #12", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(5L), organizer, List.of(user)),
                EventCreator.create("Java Dev Talks #13", eventImageRepository.save(EventImageCreator.createDefaultEventImage()), DATE_TIME.plusWeeks(6L), organizer, List.of(user))
        );
        eventRepository.saveAll(events);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/events/my_events/cities/rzeszow");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)));
    }
}
