package pl.patrykdepka.iteventsapi.appuser.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserTableDTO;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createUser;
import static pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator.createNewProfileData;
import static pl.patrykdepka.iteventsapi.creator.CreateUserDTOCreator.createNewUserData;

class AppUserServiceIT extends BaseIT {

    @Autowired
    private AppUserService appUserService;

    @Test
    void shouldReturnTrueWhenUserWithGivenEmailExists() {
        // given
        var email = "patrykkowalski@example.com";
        // when
        boolean isExists = appUserService.checkIfUserExists(email);
        // then
        assertThat(isExists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUserWithGivenEmailDoesNotExists() {
        // given
        var email = "patryktestowy@example.com";
        // when
        boolean isExists = appUserService.checkIfUserExists(email);
        // then
        assertThat(isExists).isFalse();
    }

    @Test
    void shouldCreateUser() {
        // given
        var newUserData = createNewUserData(
                "Patryk", "Testowy", "1995-10-06", "qwerty", "qwerty"
        );
        // when
        var createdUser = appUserService.createUser(newUserData);
        // then
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getContentType()).isEqualTo("image/png");
        assertThat(createdUser.getProfileImageData()).isNotNull();
        assertThat(createdUser.getFirstName()).isEqualTo(newUserData.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(newUserData.getLastName());
        assertThat(createdUser.getDateOfBirth()).isEqualTo(newUserData.getDateOfBirth());
        assertThat(createdUser.getCity()).isBlank();
        assertThat(createdUser.getBio()).isBlank();
    }

    @Test
    void shouldReturnProfileForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        var userProfile = appUserService.findUserProfileById(id);
        // then
        assertThat(userProfile.getId()).isEqualTo(id);
        assertThat(userProfile.getContentType()).isEqualTo("image/png");
        assertThat(userProfile.getProfileImageData()).isNotNull();
        assertThat(userProfile.getFirstName()).isEqualTo("Patryk");
        assertThat(userProfile.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfile.getDateOfBirth()).isEqualTo("1995-10-06");
        assertThat(userProfile.getCity()).isEqualTo("Rzeszów");
        assertThat(userProfile.getBio()).isEqualTo("Cześć! Nazywam się Patryk Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na WSIiZ.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnUsers(int pageNumber) {
        // given
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<AppUserTableDTO> users = appUserService.findAllUsers(page);
        // then
        assertThat(users.getTotalPages()).isEqualTo(2);
        assertThat(users.getTotalElements()).isEqualTo(15);
        assertThat(users.getNumber()).isEqualTo(pageNumber);
    }

    @Test
    void shouldReturnUsersWithGivenLastName() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<AppUserTableDTO> users = appUserService.findUsersBySearch("kowalski", page);
        // then
        assertThat(users.getTotalPages()).isEqualTo(1);
        assertThat(users.getTotalElements()).isEqualTo(3);
        assertThat(users.getNumber()).isEqualTo(0);
        users.getContent().forEach(u -> assertThat(u.getLastName()).isEqualTo("Kowalski"));
    }

    @Test
    void shouldReturnUsersWithGivenFirstNameAndLastName() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<AppUserTableDTO> users = appUserService.findUsersBySearch("patryk kowalski", page);
        // then
        assertThat(users.getTotalPages()).isEqualTo(1);
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertThat(users.getNumber()).isEqualTo(0);
        assertThat(users.getContent().get(0).getFirstName()).isEqualTo("Patryk");
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnProfileForGivenUser() {
        // given
        var user = createUser();
        // when
        var userProfile = appUserService.findUserProfile(user);
        // then
        assertThat(userProfile.getId()).isEqualTo(user.getId());
        assertThat(userProfile.getContentType()).isEqualTo("image/png");
        assertThat(userProfile.getProfileImageData()).isNotBlank();
        assertThat(userProfile.getFirstName()).isEqualTo("User");
        assertThat(userProfile.getLastName()).isEqualTo("User");
        assertThat(userProfile.getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(userProfile.getCity()).isEqualTo("Rzeszów");
        assertThat(userProfile.getBio()).isEqualTo("Cześć! Jestem domyślnym użytkownikiem tego serwisu.");
    }

    @Test
    void shouldReturnProfileDataForGivenUser() {
        // given
        var user = createUser();
        // when
        var userProfileDataToEdit = appUserService.findUserProfileData(user);
        // then
        assertThat(userProfileDataToEdit.getCurrentProfileImageData()).isNotBlank();
        assertThat(userProfileDataToEdit.getProfileImage()).isNull();
        assertThat(userProfileDataToEdit.getFirstName()).isEqualTo("User");
        assertThat(userProfileDataToEdit.getLastName()).isEqualTo("User");
        assertThat(userProfileDataToEdit.getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(userProfileDataToEdit.getCity()).isEqualTo("Rzeszów");
        assertThat(userProfileDataToEdit.getBio()).isEqualTo("Cześć! Jestem domyślnym użytkownikiem tego serwisu.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedProfileDataForGivenUser() {
        // given
        var user = createUser();
        var newProfileData = createNewProfileData(null, "Testy Wielkie", "Testowy biogram");
        // when
        var updatedProfileData = appUserService.updateUserProfileData(user, newProfileData);
        // then
        assertThat(updatedProfileData.getCurrentProfileImageData()).isNotBlank();
        assertThat(updatedProfileData.getProfileImage()).isNull();
        assertThat(updatedProfileData.getFirstName()).isEqualTo("User");
        assertThat(updatedProfileData.getLastName()).isEqualTo("User");
        assertThat(updatedProfileData.getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(updatedProfileData.getCity()).isEqualTo(newProfileData.getCity());
        assertThat(updatedProfileData.getBio()).isEqualTo(newProfileData.getBio());
    }

    @Test
    void shouldUpdatePasswordForGivenUser() {
        // given
        var user = createUser();
        var newPasswordData = new AppUserPasswordEditDTO(
                "qwerty", "ytrewq", "ytrewq"
        );
        // when
        appUserService.updateUserPassword(user, newPasswordData);
        // then
        assertThat(passwordEncoder.matches(newPasswordData.getNewPassword(), user.getPassword())).isTrue();
    }
}
