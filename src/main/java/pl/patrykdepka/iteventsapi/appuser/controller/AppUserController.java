package pl.patrykdepka.iteventsapi.appuser.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.facade.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.appuser.service.AppUserService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class AppUserController {
    private final AppUserService appUserService;
    private final CurrentUserFacade currentUserFacade;

    public AppUserController(AppUserService appUserService, CurrentUserFacade currentUserFacade) {
        this.appUserService = appUserService;
        this.currentUserFacade = currentUserFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserProfileDTO> register(@Valid @RequestBody AppUserRegistrationDTO newUserData) {
        AppUserProfileDTO savedUser = appUserService.createUser(newUserData);
        URI savedUserUri = ServletUriComponentsBuilder
                .fromUriString("/api/v1/users")
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(savedUserUri).body(savedUser);
    }

    @GetMapping("/profile")
    public AppUserProfileDTO getUserProfile() {
        return appUserService.findUserProfile(currentUserFacade.getCurrentUser());
    }

    @GetMapping("/users")
    public Page<AppUserTableDTO> getAllUsers(@RequestParam(name = "page", required = false) Integer pageNumber,
                                             @RequestParam(name = "sort_by", required = false) String sortProperty,
                                             @RequestParam(name = "order_by", required = false) String sortDirection) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
        String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return appUserService.findAllUsers(pageRequest);
    }

    @GetMapping("/users/results")
    public Page<AppUserTableDTO> getUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                                  @RequestParam(name = "page", required = false) Integer pageNumber,
                                                  @RequestParam(name = "sort_by", required = false) String sortProperty,
                                                  @RequestParam(name = "order_by", required = false) String sortDirection) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
        String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "ASC";
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
        return appUserService.findUsersBySearch(searchQuery, pageRequest);
    }

    @GetMapping("/users/{id}")
    public AppUserProfileDTO getUserProfile(@PathVariable Long id) {
        return appUserService.findUserProfileByUserId(id);
    }

    @GetMapping("/settings/profile")
    public AppUserProfileEditDTO showUserProfileEditForm() {
        return appUserService.findUserProfileToEdit(currentUserFacade.getCurrentUser());
    }

    @PatchMapping("/settings/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserProfile(@Valid @RequestBody AppUserProfileEditDTO userProfile) {
        appUserService.updateUserProfile(currentUserFacade.getCurrentUser(), userProfile);
    }

    @PatchMapping("/settings/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(@Valid @RequestBody AppUserPasswordEditDTO newUserPassword) {
        appUserService.updateUserPassword(currentUserFacade.getCurrentUser(), newUserPassword);
    }
}
