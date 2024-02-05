package pl.patrykdepka.iteventsapi.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.patrykdepka.iteventsapi.appuser.domain.AdminAppUserService;
import pl.patrykdepka.iteventsapi.appuser.domain.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserTableDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminDeleteAppUserDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class AdminAppUserController {
    private final AdminAppUserService adminAppUserService;
    private final CurrentUserFacade currentUserFacade;

    @GetMapping("/admin/users")
    Page<AdminAppUserTableDTO> getAllUsers(
            @RequestParam(name = "page", required = false) Integer pageNumber,
            @RequestParam(name = "sort_by", required = false) String sortProperty,
            @RequestParam(name = "order_by", required = false) String sortDirection
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !sortProperty.isEmpty() ? sortProperty : "lastName";
        String direction = sortDirection != null && !sortDirection.isEmpty() ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return adminAppUserService.findAllUsers(pageRequest);
    }

    @GetMapping("/admin/users/results")
    Page<AdminAppUserTableDTO> getUsersBySearch(
            @RequestParam(name = "search_query", required = false) String searchQuery,
            @RequestParam(name = "page", required = false) Integer pageNumber,
            @RequestParam(name = "sort_by", required = false) String sortProperty,
            @RequestParam(name = "order_by", required = false) String sortDirection
    ) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !sortProperty.isEmpty() ? sortProperty : "lastName";
        String direction = sortDirection != null && !sortDirection.isEmpty() ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return adminAppUserService.findUsersBySearch(searchQuery, pageRequest);
    }

    @GetMapping("/admin/users/{id}/account")
    AdminAppUserAccountEditDTO getUserAccountData(@PathVariable Long id) {
        return adminAppUserService.findUserAccountData(id);
    }

    @PutMapping("/admin/users/{id}/account")
    AdminAppUserAccountEditDTO updateUserAccountData(@PathVariable Long id, @RequestBody @Valid AdminAppUserAccountEditDTO newAccountData) {
        return adminAppUserService.updateUserAccountData(currentUserFacade.getCurrentUser(), id, newAccountData);
    }

    @GetMapping("/admin/users/{id}/profile")
    AppUserProfileEditDTO getUserProfileData(@PathVariable Long id) {
        return adminAppUserService.findUserProfileData(id);
    }

    @PutMapping("/admin/users/{id}/profile")
    AppUserProfileEditDTO updateUserProfileData(@PathVariable Long id, @RequestBody @Valid AppUserProfileEditDTO newProfileData) {
        return adminAppUserService.updateUserProfileData(currentUserFacade.getCurrentUser(), id, newProfileData);
    }

    @PutMapping("/admin/users/{id}/password")
    @ResponseStatus(NO_CONTENT)
    void updateUserPassword(@PathVariable Long id, @RequestBody @Valid AdminAppUserPasswordEditDTO newPasswordData) {
        adminAppUserService.updateUserPassword(currentUserFacade.getCurrentUser(), id, newPasswordData);
    }

    @DeleteMapping("/admin/users/{id}")
    @ResponseStatus(NO_CONTENT)
    void deleteUser(@PathVariable Long id, @RequestBody @Valid AdminDeleteAppUserDTO deleteUserData) {
        adminAppUserService.deleteUser(currentUserFacade.getCurrentUser(), id, deleteUserData);
    }
}
