package pl.patrykdepka.iteventsapi.appuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserRegistrationDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserRegistrationDTOCreator;
import pl.patrykdepka.iteventsapi.creator.ProfileImageCreator;
import pl.patrykdepka.iteventsapi.image.domain.ImageRepository;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ADMIN;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ORGANIZER;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void shouldCreateUser() throws Exception {
        // given
        AppUserRegistrationDTO newUserData = AppUserRegistrationDTOCreator.create();
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserData));
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        // then
        assertThat(result.getResponse().getHeader("Location")).isNotNull();
        assertThat(appUserRepository.existsByEmail(newUserData.getEmail())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUserProfile() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/profile");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        AppUserProfileDTO returnedUserProfile = objectMapper.readValue(result.getResponse().getContentAsString(), AppUserProfileDTO.class);
        assertThat(returnedUserProfile.getProfileImageType()).isEqualTo(user.getProfileImage().getType());
        assertThat(returnedUserProfile.getProfileImageData()).isEqualTo(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()));
        assertThat(returnedUserProfile.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfile.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfile.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(returnedUserProfile.getCity()).isEqualTo(user.getCity());
        assertThat(returnedUserProfile.getBio()).isEqualTo(user.getBio());
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnAllUsers() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
                AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Patryk", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Jan", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER),
                AppUserCreator.create("Patryk", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Wysocki", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Polak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Zuzanna", "Kowalska", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Michalik", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Daniel", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Maria", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()))
        );
        appUserRepository.saveAll(users);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/users");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnEmptyPageOfUsersIfSearchQueryIsEmpty() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
                AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Patryk", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Jan", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER),
                AppUserCreator.create("Patryk", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Wysocki", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Polak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Zuzanna", "Kowalska", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Michalik", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Daniel", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Maria", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()))
        );
        appUserRepository.saveAll(users);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/users/results")
                .param("search_query", "");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUsersBySearchIfSearchQueryHasOneWord() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
                AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Patryk", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Jan", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER),
                AppUserCreator.create("Patryk", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Wysocki", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Polak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Zuzanna", "Kowalska", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Michalik", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Daniel", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Maria", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()))
        );
        appUserRepository.saveAll(users);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/users/results")
                .param("search_query", "kowalski");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUsersBySearchIfSearchQueryHasTwoWord() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
                AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Patryk", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Jan", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ORGANIZER),
                AppUserCreator.create("Patryk", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Wysocki", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Polak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Zuzanna", "Kowalska", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Piotr", "Michalik", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Dawid", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Daniel", "Dąbrowski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage())),
                AppUserCreator.create("Maria", "Nowak", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()))
        );
        appUserRepository.saveAll(users);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/users/results")
                .param("search_query", "jan kowalski");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUserProfileByUserId() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/users/" + user.getId());
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        AppUserProfileDTO returnedUserProfile = objectMapper.readValue(result.getResponse().getContentAsString(), AppUserProfileDTO.class);
        assertThat(returnedUserProfile.getProfileImageType()).isEqualTo(user.getProfileImage().getType());
        assertThat(returnedUserProfile.getProfileImageData()).isEqualTo(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()));
        assertThat(returnedUserProfile.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfile.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfile.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(returnedUserProfile.getCity()).isEqualTo(user.getCity());
        assertThat(returnedUserProfile.getBio()).isEqualTo(user.getBio());
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUserProfileToEdit() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/settings/profile");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        AppUserProfileEditDTO returnedUserProfileToEdit = objectMapper.readValue(result.getResponse().getContentAsString(), AppUserProfileEditDTO.class);
        assertThat(returnedUserProfileToEdit.getProfileImage()).isNull();
        assertThat(returnedUserProfileToEdit.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfileToEdit.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfileToEdit.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(returnedUserProfileToEdit.getCity()).isNull();
        assertThat(returnedUserProfileToEdit.getBio()).isNull();
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldReturnUpdatedUserProfile() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        AppUserProfileEditDTO newUserProfileData = AppUserProfileEditDTOCreator.create(
                user,
                "Kraków",
                "Cześć!");
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/v1/settings/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserProfileData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
        // then
        AppUser userAfterUpdate = appUserRepository.findById(user.getId()).get();
        assertThat(userAfterUpdate.getCity()).isEqualTo(newUserProfileData.getCity());
        assertThat(userAfterUpdate.getBio()).isEqualTo(newUserProfileData.getBio());
    }

    @Test
    @Transactional
    @WithMockUser(username = "jankowalski@example.com")
    void shouldUpdateUserPassword() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        AppUserPasswordEditDTO newUserPasswordData = new AppUserPasswordEditDTO("tests", "qwerty", "qwerty");
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/v1/settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserPasswordData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
        // then
        AppUser userAfterUpdate = appUserRepository.findById(user.getId()).get();
        assertThat(passwordEncoder.matches(newUserPasswordData.getNewPassword(), userAfterUpdate.getPassword())).isTrue();
    }
}
