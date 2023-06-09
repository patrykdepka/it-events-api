package pl.patrykdepka.iteventsapi.appuser.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

public interface AppUserService {

    boolean checkIfUserExists(String email);

    AppUserProfileDTO createUser(AppUserRegistrationDTO newUserData);

    AppUserProfileDTO findUserProfile(AppUser currentUser);

    Page<AppUserTableDTO> findAllUsers(Pageable page);

    Page<AppUserTableDTO> findUsersBySearch(String searchQuery, Pageable page);

    AppUserProfileDTO findUserProfileByUserId(Long id);

    AppUserProfileEditDTO findUserProfileToEdit(AppUser currentUser);

    AppUserProfileEditDTO updateUserProfile(AppUser currentUser, AppUserProfileEditDTO userProfile);

    void updateUserPassword(AppUser currentUser, AppUserPasswordEditDTO newUserPassword);
}
