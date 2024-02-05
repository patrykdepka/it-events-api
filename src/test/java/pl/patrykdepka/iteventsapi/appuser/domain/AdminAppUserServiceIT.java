package pl.patrykdepka.iteventsapi.appuser.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserTableDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminDeleteAppUserDTO;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_ORGANIZER;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_USER;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createAdmin;
import static pl.patrykdepka.iteventsapi.creator.AppUserProfileEditDTOCreator.createNewProfileData;

class AdminAppUserServiceIT extends BaseIT {

    @Autowired
    private AdminAppUserService adminAppUserService;

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnUsers(int pageNumber) {
        // given
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<AdminAppUserTableDTO> users = adminAppUserService.findAllUsers(page);
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
        Page<AdminAppUserTableDTO> users = adminAppUserService.findUsersBySearch("kowalski", page);
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
        Page<AdminAppUserTableDTO> users = adminAppUserService.findUsersBySearch("patryk kowalski", page);
        // then
        assertThat(users.getTotalPages()).isEqualTo(1);
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertThat(users.getNumber()).isEqualTo(0);
        assertThat(users.getContent().get(0).getFirstName()).isEqualTo("Patryk");
        assertThat(users.getContent().get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    void shouldReturnAccountDataToEditForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        var userAccountDataToEdit = adminAppUserService.findUserAccountData(id);
        // then
        assertThat(userAccountDataToEdit.isEnabled()).isTrue();
        assertThat(userAccountDataToEdit.isAccountNonLocked()).isTrue();
        assertThat(userAccountDataToEdit.getRoles().size()).isEqualTo(1);
        assertThat(userAccountDataToEdit.getRoles().contains(ROLE_USER)).isTrue();
    }

    @Test
    void shouldUpdateAndReturnUpdatedAccountDataForUserWithGivenId() {
        // given
        var id = 5L;
        var newAccountData = new AdminAppUserAccountEditDTO(false, false, List.of(ROLE_ORGANIZER));
        // when
        var updatedAccountData = adminAppUserService.updateUserAccountData(createAdmin(), id, newAccountData);
        // then
        assertThat(updatedAccountData.isEnabled()).isFalse();
        assertThat(updatedAccountData.isAccountNonLocked()).isFalse();
        assertThat(updatedAccountData.getRoles().size()).isEqualTo(1);
        assertThat(updatedAccountData.getRoles()).isEqualTo(List.of(ROLE_ORGANIZER));
    }

    @Test
    void shouldReturnProfileDataToEditForUserWithGivenId() {
        // given
        var id = 5L;
        // when
        var userProfileDataToEdit = adminAppUserService.findUserProfileData(id);
        // then
        assertThat(userProfileDataToEdit.getCurrentProfileImageData()).isNotBlank();
        assertThat(userProfileDataToEdit.getProfileImage()).isNull();
        assertThat(userProfileDataToEdit.getFirstName()).isEqualTo("Patryk");
        assertThat(userProfileDataToEdit.getLastName()).isEqualTo("Kowalski");
        assertThat(userProfileDataToEdit.getDateOfBirth()).isEqualTo("1995-10-06");
        assertThat(userProfileDataToEdit.getCity()).isEqualTo("Rzeszów");
        assertThat(userProfileDataToEdit.getBio()).isEqualTo("Cześć! Nazywam się Patryk Kowalski i mieszkam w Rzeszowie. Jestem absolwentem informatyki na WSIiZ.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedProfileDataForUserWithGivenId() {
        // given
        var id = 5L;
        var newProfileData = createNewProfileData(
                null, "Testowy", "Użytkownik", "2000-01-01", "Testy Wielkie", "Testowy biogram"
        );
        // when
        var updatedProfileData = adminAppUserService.updateUserProfileData(createAdmin(), id, newProfileData);
        // then
        assertThat(updatedProfileData.getCurrentProfileImageData()).isNotBlank();
        assertThat(updatedProfileData.getProfileImage()).isNull();
        assertThat(updatedProfileData.getFirstName()).isEqualTo(newProfileData.getFirstName());
        assertThat(updatedProfileData.getLastName()).isEqualTo(newProfileData.getLastName());
        assertThat(updatedProfileData.getDateOfBirth()).isEqualTo(newProfileData.getDateOfBirth());
        assertThat(updatedProfileData.getCity()).isEqualTo(newProfileData.getCity());
        assertThat(updatedProfileData.getBio()).isEqualTo(newProfileData.getBio());
    }

    @Test
    void shouldUpdateUserPasswordForUserWithGivenId() {
        // given
        var admin = createAdmin();
        var id = 5L;
        var newPasswordData = new AdminAppUserPasswordEditDTO(
                "qwerty", "ytrewq", "ytrewq"
        );
        // when
        adminAppUserService.updateUserPassword(admin, id, newPasswordData);
        // then
        AppUser user = appUserRepository.findById(id).get(); // może zrobić metodę getById?
        assertThat(passwordEncoder.matches(newPasswordData.getNewPassword(), user.getPassword())).isTrue();
    }

    @Test
    void shouldDeleteUserWithGivenId() {
        // given
        var id = 5L;
        var deleteUserData = new AdminDeleteAppUserDTO("qwerty");
        // when
        adminAppUserService.deleteUser(createAdmin(), id, deleteUserData);
        // then
        assertThat(appUserRepository.findById(id)).isEmpty();
    }
}
