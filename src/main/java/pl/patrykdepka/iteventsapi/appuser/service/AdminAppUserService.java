package pl.patrykdepka.iteventsapi.appuser.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

public interface AdminAppUserService {

    Page<AdminAppUserTableDTO> findAllUsers(Pageable page);

    Page<AdminAppUserTableDTO> findUsersBySearch(String searchQuery, Pageable page);

    AdminAppUserAccountEditDTO findUserAccountToEdit(Long id);

    AdminAppUserAccountEditDTO updateUserAccount(Long id, AdminAppUserAccountEditDTO userAccount);

    AdminAppUserProfileEditDTO findUserProfileToEdit(Long id);

    AdminAppUserProfileEditDTO updateUserProfile(Long id, AdminAppUserProfileEditDTO userProfile);

    AdminAppUserPasswordEditDTO updateUserPassword(AppUser currentUser, Long id, AdminAppUserPasswordEditDTO newUserPassword);

    void deleteUser(AppUser currentUser, AdminDeleteAppUserDTO deleteUserData);
}
