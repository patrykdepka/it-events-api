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
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminDeleteAppUserDTO;
import pl.patrykdepka.iteventsapi.creator.AdminAppUserProfileEditDTOCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
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
class AdminAppUserControllerIntegrationTest {
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
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnAllUsers() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
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
                .get("/api/v1/admin/users");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnEmptyPageOfUsersIfSearchQueryIsEmpty() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
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
                .get("/api/v1/admin/users/results")
                .param("search_query", "");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnUsersBySearchIfSearchQueryHasOneWord() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
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
                .get("/api/v1/admin/users/results")
                .param("search_query", "kowalski");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnUsersBySearchIfSearchQueryHasTwoWord() throws Exception {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create("Admin", "", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN),
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
                .get("/api/v1/admin/users/results")
                .param("search_query", "jan kowalski");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnUserAccountToEdit() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/admin/users/" + user.getId() + "/account");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        AdminAppUserAccountEditDTO returnedUserAccountToEdit = objectMapper.readValue(result.getResponse().getContentAsString(), AdminAppUserAccountEditDTO.class);
        assertThat(returnedUserAccountToEdit.isEnabled()).isEqualTo(user.isEnabled());
        assertThat(returnedUserAccountToEdit.isAccountNonLocked()).isEqualTo(user.isAccountNonLocked());
        assertThat(returnedUserAccountToEdit.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminadmin@example.com", roles = {"ADMIN"})
    void shouldReturnUpdatedUserAccount() throws Exception {
        // given
        AppUser admin = AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        List<AppUser> users = List.of(
                admin,
                user
        );
        appUserRepository.saveAll(users);
        AdminAppUserAccountEditDTO newUserAccountData = AdminAppUserAccountEditDTO.builder()
                .enabled(false)
                .accountNonLocked(false)
                .roles(List.of(ROLE_ORGANIZER))
                .build();
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/v1/admin/users/" + user.getId() + "/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserAccountData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminadmin@example.com", roles = {"ADMIN"})
    void shouldReturnUserProfileToEdit() throws Exception {
        // given
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        appUserRepository.save(user);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/admin/users/" + user.getId() + "/profile");
        MvcResult result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        AdminAppUserProfileEditDTO returnedUserProfileToEdit = objectMapper.readValue(result.getResponse().getContentAsString(), AdminAppUserProfileEditDTO.class);
        assertThat(returnedUserProfileToEdit.getProfileImage()).isNull();
        assertThat(returnedUserProfileToEdit.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfileToEdit.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfileToEdit.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(returnedUserProfileToEdit.getCity()).isNull();
        assertThat(returnedUserProfileToEdit.getBio()).isNull();
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminadmin@example.com", roles = {"ADMIN"})
    void shouldReturnUpdatedUserProfile() throws Exception {
        // given
        AppUser admin = AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        List<AppUser> users = List.of(
                admin,
                user
        );
        appUserRepository.saveAll(users);
        AdminAppUserProfileEditDTO newUserProfileData = AdminAppUserProfileEditDTOCreator.create();
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/v1/admin/users/" + user.getId() + "/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserProfileData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
        // then
        AppUser userAfterUpdate = appUserRepository.findById(user.getId()).get();
        String updatedProfileImage = "data:" + userAfterUpdate.getProfileImage().getContentType() + ";base64," + Base64.getEncoder().encodeToString(userAfterUpdate.getProfileImage().getFileData());
        String newProfileImage = "data:" + newUserProfileData.getProfileImage().getContentType() + ";base64," + newUserProfileData.getProfileImage().getContent();
        assertThat(updatedProfileImage).isEqualTo(newProfileImage);
        assertThat(userAfterUpdate.getFirstName()).isEqualTo(newUserProfileData.getFirstName());
        assertThat(userAfterUpdate.getLastName()).isEqualTo(newUserProfileData.getLastName());
        assertThat(userAfterUpdate.getDateOfBirth()).isEqualTo(newUserProfileData.getDateOfBirth());
        assertThat(userAfterUpdate.getCity()).isEqualTo(newUserProfileData.getCity());
        assertThat(userAfterUpdate.getBio()).isEqualTo(newUserProfileData.getBio());
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminadmin@example.com", roles = {"ADMIN"})
    void shouldUpdateUserPassword() throws Exception {
        // given
        AppUser admin = AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        List<AppUser> users = List.of(
                admin,
                user
        );
        appUserRepository.saveAll(users);
        AdminAppUserPasswordEditDTO newUserPasswordData = new AdminAppUserPasswordEditDTO(user.getId(), "tests", "qwerty", "qwerty");
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/v1/admin/users/" + user.getId() + "/settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserPasswordData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
        // then
        AppUser userAfterUpdate = appUserRepository.findById(user.getId()).get();
        assertThat(passwordEncoder.matches(newUserPasswordData.getNewPassword(), userAfterUpdate.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser(username = "adminadmin@example.com", roles = {"ADMIN"})
    void shouldDeleteUser() throws Exception {
        // given
        AppUser admin = AppUserCreator.create("Admin", "Admin", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()), ROLE_ADMIN);
        AppUser user = AppUserCreator.create("Jan", "Kowalski", imageRepository.save(ProfileImageCreator.createDefaultProfileImage()));
        List<AppUser> users = List.of(
                admin,
                user
        );
        appUserRepository.saveAll(users);
        AdminDeleteAppUserDTO deleteUserData = new AdminDeleteAppUserDTO(user.getId(), "tests");
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/api/v1/admin/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteUserData));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());
        // then
        assertThat(appUserRepository.existsById(user.getId())).isFalse();
    }
}
