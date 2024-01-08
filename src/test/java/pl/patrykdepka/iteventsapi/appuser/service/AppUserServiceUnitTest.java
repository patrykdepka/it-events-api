package pl.patrykdepka.iteventsapi.appuser.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserRepository;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUserService;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.*;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator;
import pl.patrykdepka.iteventsapi.creator.AppUserRegistrationDTOCreator;
import pl.patrykdepka.iteventsapi.creator.ProfileImageCreator;
import pl.patrykdepka.iteventsapi.profileimage.service.ProfileImageService;
import pl.patrykdepka.iteventsapi.security.AppUserDetailsService;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ADMIN;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_USER;

class AppUserServiceUnitTest {
    static final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "lastName"));
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private ProfileImageService profileImageService;
    private AppUserDetailsService appUserDetailsService;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        appUserRepository = Mockito.mock(AppUserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        profileImageService = Mockito.mock(ProfileImageService.class);
        appUserDetailsService = Mockito.mock(AppUserDetailsService.class);
        appUserService = new AppUserService(
                appUserRepository,
                passwordEncoder,
                profileImageService,
                appUserDetailsService
        );
    }

    @Test
    void shouldReturnTrueIfUserExists() {
        // given
        String email = "jankowalski@example.com";
        when(appUserRepository.existsByEmail(email)).thenReturn(true);
        // when
        boolean isUserExists = appUserService.checkIfUserExists(email);
        // then
        assertThat(isUserExists).isTrue();
    }

    @Test
    void shouldReturnFalseIfUserDoesNotExists() {
        // given
        String email = "jankowalski@example.com";
        when(appUserRepository.existsByEmail(email)).thenReturn(false);
        // when
        boolean isUserExists = appUserService.checkIfUserExists(email);
        // then
        assertThat(isUserExists).isFalse();
    }

    @Test
    void shouldCreateUser() {
        // given
        AppUserRegistrationDTO newUserData = AppUserRegistrationDTOCreator.create();
        when(profileImageService.createDefaultProfileImage()).thenReturn(ProfileImageCreator.createDefaultProfileImage());
        when(passwordEncoder.encode(newUserData.getPassword())).thenReturn("{bcrypt}$2a$10$r7EjB7rf4j4SJ/ZVYUVT6.AIcaz6VNOGqNGr6mWAURZleQS2bSLie");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(i -> i.getArguments()[0]);
        // when
        appUserService.createUser(newUserData);
        // then
        verify(appUserRepository, times(1)).save(argThat((AppUser savedUser) -> {
            Assertions.assertAll("Testing saved user",
                    () -> assertNull(savedUser.getId()),
                    () -> assertNotNull(savedUser.getProfileImage()),
                    () -> assertEquals(newUserData.getFirstName(), savedUser.getFirstName()),
                    () -> assertEquals(newUserData.getLastName(), savedUser.getLastName()),
                    () -> assertEquals(newUserData.getDateOfBirth(), savedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                    () -> assertNull(savedUser.getCity()),
                    () -> assertNull(savedUser.getBio()),
                    () -> assertEquals(newUserData.getEmail(), savedUser.getEmail()),
                    () -> assertEquals(passwordEncoder.encode(newUserData.getPassword()), savedUser.getPassword()),
                    () -> assertThat(savedUser.isEnabled()).isTrue(),
                    () -> assertThat(savedUser.isAccountNonLocked()).isTrue(),
                    () -> assertEquals(List.of(ROLE_USER), savedUser.getRoles())
            );
            return true;
        }));
    }

    @Test
    void shouldReturnUserProfile() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        // when
        AppUserProfileDTO returnedUserProfile = appUserService.findUserProfile(user);
        // then
        assertThat(returnedUserProfile.getProfileImageType()).isEqualTo(user.getProfileImage().getFileType());
        assertThat(returnedUserProfile.getProfileImageData()).isEqualTo(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()));
        assertThat(returnedUserProfile.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfile.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfile.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(returnedUserProfile.getCity()).isEqualTo(user.getCity());
        assertThat(returnedUserProfile.getBio()).isEqualTo(user.getBio());
    }

    @Test
    void shouldReturnAllUsers() {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create(1L, "Admin", "Admin", ROLE_ADMIN),
                AppUserCreator.create(2L, "Jan", "Kowalski"),
                AppUserCreator.create(3L, "Patryk", "Kowalski")
        );
        when(appUserRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(users, pageRequest, users.size()));
        // when
        Page<AppUserTableDTO> returnedUsers = appUserService.findAllUsers(pageRequest);
        // then
        assertThat(returnedUsers).isNotEmpty();
        assertThat(returnedUsers).hasSize(3);
        assertThat(returnedUsers.getContent().get(0).getLastName()).isEqualTo("Admin");
        assertThat(returnedUsers.getContent().get(1).getLastName()).isEqualTo("Kowalski");
        assertThat(returnedUsers.getContent().get(2).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnEmptyPageOfUsersIfSearchQueryIsEmpty() {
        // given
        String searchQuery = "";
        // when
        Page<AppUserTableDTO> returnedUsers = appUserService.findUsersBySearch(searchQuery, pageRequest);
        // then
        assertThat(returnedUsers).isEmpty();
    }

    @Test
    void shouldReturnUsersBySearchIfSearchQueryHasOneWord() {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create(2L, "Jan", "Kowalski"),
                AppUserCreator.create(3L, "Patryk", "Kowalski")
        );
        when(appUserRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(new PageImpl<>(users, pageRequest, users.size()));
        // when
        Page<AppUserTableDTO> returnedUsers = appUserService.findUsersBySearch("kowalski", pageRequest);
        // then
        assertThat(returnedUsers).isNotEmpty();
        assertThat(returnedUsers).hasSize(2);
        assertThat(returnedUsers.getContent().get(0).getLastName()).isEqualTo("Kowalski");
        assertThat(returnedUsers.getContent().get(1).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnUsersBySearchIfSearchQueryHasTwoWord() {
        // given
        List<AppUser> users = List.of(
                AppUserCreator.create(2L, "Jan", "Kowalski")
        );
        when(appUserRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(new PageImpl<>(users, pageRequest, users.size()));
        // when
        Page<AppUserTableDTO> returnedUsers = appUserService.findUsersBySearch("jan kowalski", pageRequest);
        // then
        assertThat(returnedUsers).isNotEmpty();
        assertThat(returnedUsers).hasSize(1);
        assertThat(returnedUsers.getContent().get(0).getFirstName()).isEqualTo("Jan");
        assertThat(returnedUsers.getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnUserProfileByUserId() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        when(appUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        AppUserProfileDTO returnedUserProfile = appUserService.findUserProfileByUserId(user.getId());
        // then
        assertThat(returnedUserProfile).isNotNull();
        assertThat(returnedUserProfile.getProfileImageType()).isEqualTo(user.getProfileImage().getFileType());
        assertThat(returnedUserProfile.getProfileImageData()).isEqualTo(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()));
        assertThat(returnedUserProfile.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfile.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfile.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(returnedUserProfile.getCity()).isEqualTo(user.getCity());
        assertThat(returnedUserProfile.getBio()).isEqualTo(user.getBio());
    }

    @Test
    void shouldReturnUserProfileToEdit() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        // when
        AppUserProfileEditDTO returnedUserProfileToEdit = appUserService.findUserProfileToEdit(user);
        // then
        assertThat(returnedUserProfileToEdit).isNotNull();
        assertThat(returnedUserProfileToEdit.getProfileImage()).isNull();
        assertThat(returnedUserProfileToEdit.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(returnedUserProfileToEdit.getLastName()).isEqualTo(user.getLastName());
        assertThat(returnedUserProfileToEdit.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(returnedUserProfileToEdit.getCity()).isNull();
        assertThat(returnedUserProfileToEdit.getBio()).isNull();
    }

    @Test
    void shouldReturnUpdatedUserProfile() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        AppUserProfileEditDTO newUserProfileData = AppUserProfileEditDTOCreator.create(user, "Rzeszów", "Cześć! Nazywam się Jan Kowalski i jestem z Rzeszowa.");
        // when
        AppUserProfileEditDTO updatedUserProfile = appUserService.updateUserProfile(user, newUserProfileData);
        // then
        assertThat(updatedUserProfile).isNotNull();
        assertThat(updatedUserProfile.getProfileImage()).isNull();
        assertThat(updatedUserProfile.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(updatedUserProfile.getLastName()).isEqualTo(user.getLastName());
        assertThat(updatedUserProfile.getDateOfBirth()).isEqualTo(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(updatedUserProfile.getCity()).isEqualTo(newUserProfileData.getCity());
        assertThat(updatedUserProfile.getBio()).isEqualTo(newUserProfileData.getBio());
    }

    @Test
    void shouldUpdateUserPassword() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        AppUserPasswordEditDTO newUserPasswordData = new AppUserPasswordEditDTO("tests", "qwerty", "qwerty");
        when(passwordEncoder.matches(newUserPasswordData.getCurrentPassword(), user.getPassword())).thenReturn(true);
        // when
        appUserService.updateUserPassword(user, newUserPasswordData);
        // then
        verify(passwordEncoder, times(1)).encode(newUserPasswordData.getNewPassword());
    }

    @Test
    void shouldThrowExceptionIfCurrentPasswordIsIncorrect() {
        // given
        AppUser user = AppUserCreator.create(2L, "Jan", "Kowalski");
        AppUserPasswordEditDTO newUserPasswordData = new AppUserPasswordEditDTO("qwerty", "tests", "tests");
        // then
        assertThatThrownBy(() -> appUserService.updateUserPassword(user, newUserPasswordData))
                .isInstanceOf(IncorrectCurrentPasswordException.class);
    }
}
