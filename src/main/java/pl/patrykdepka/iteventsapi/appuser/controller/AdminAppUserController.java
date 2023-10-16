package pl.patrykdepka.iteventsapi.appuser.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.facade.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.appuser.service.AdminAppUserService;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1")
public class AdminAppUserController {
    private final AdminAppUserService adminAppUserService;
    private final CurrentUserFacade currentUserFacade;
    private final MessageSource messageSource;

    public AdminAppUserController(
            AdminAppUserService adminAppUserService,
            CurrentUserFacade currentUserFacade,
            MessageSource messageSource
    ) {
        this.adminAppUserService = adminAppUserService;
        this.currentUserFacade = currentUserFacade;
        this.messageSource = messageSource;
    }

    @GetMapping("/admin/users")
    public Page<AdminAppUserTableDTO> getAllUsers(@RequestParam(name = "page", required = false) Integer pageNumber,
                                                  @RequestParam(name = "sort_by", required = false) String sortProperty,
                                                  @RequestParam(name = "order_by", required = false) String sortDirection) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !sortProperty.isEmpty() ? sortProperty : "lastName";
        String direction = sortDirection != null && !sortDirection.isEmpty() ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return adminAppUserService.findAllUsers(pageRequest);
    }

    @GetMapping("/admin/users/results")
    public Page<AdminAppUserTableDTO> getUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                                       @RequestParam(name = "page", required = false) Integer pageNumber,
                                                       @RequestParam(name = "sort_by", required = false) String sortProperty,
                                                       @RequestParam(name = "order_by", required = false) String sortDirection) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !sortProperty.isEmpty() ? sortProperty : "lastName";
        String direction = sortDirection != null && !sortDirection.isEmpty() ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return adminAppUserService.findUsersBySearch(searchQuery, pageRequest);
    }

    @GetMapping("/admin/users/{id}/settings/account")
    public AdminAppUserAccountEditDTO showUserAccountEditForm(@PathVariable Long id) {
        return adminAppUserService.findUserAccountToEdit(id);
    }

    @PatchMapping("/admin/users/{id}/settings/account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserAccount(@PathVariable Long id, @Valid @RequestBody AdminAppUserAccountEditDTO userAccount) {
        adminAppUserService.updateUserAccount(currentUserFacade.getCurrentUser(), id, userAccount);
    }

    @GetMapping("/admin/users/{id}/settings/profile")
    public AdminAppUserProfileEditDTO showUserProfileEditForm(@PathVariable Long id) {
        return adminAppUserService.findUserProfileToEdit(id);
    }

    @PatchMapping("/admin/users/{id}/settings/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserProfile(@PathVariable Long id, @Valid @RequestBody AdminAppUserProfileEditDTO userProfile) {
        adminAppUserService.updateUserProfile(currentUserFacade.getCurrentUser(), id, userProfile);
    }

    @PatchMapping("/admin/users/{id}/settings/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @Valid @RequestBody AdminAppUserPasswordEditDTO newUserPassword) {
        try {
            adminAppUserService.updateUserPassword(currentUserFacade.getCurrentUser(), id, newUserPassword);
            return ResponseEntity.noContent().build();
        } catch (IncorrectCurrentPasswordException e) {
            return ResponseEntity.badRequest()
                    .body(
                            new IncorrectCurrentPasswordResponse(
                                    messageSource.getMessage(
                                            "form.field.currentPassword.error.invalidCurrentPassword.message",
                                            null,
                                            Locale.getDefault()
                                    )
                            )
                    );
        }
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @Valid @RequestBody AdminDeleteAppUserDTO deleteUserData) {
        try {
            adminAppUserService.deleteUser(currentUserFacade.getCurrentUser(), deleteUserData);
            return ResponseEntity.noContent().build();
        } catch (IncorrectCurrentPasswordException e) {
            return ResponseEntity.badRequest()
                    .body(
                            new IncorrectCurrentPasswordResponse(
                                    messageSource.getMessage(
                                            "form.field.currentPassword.error.invalidCurrentPassword.message",
                                            null,
                                            Locale.getDefault()
                                    )
                            )
                    );
        }
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private class IncorrectCurrentPasswordResponse {
        private final String adminPassword;
    }
}
