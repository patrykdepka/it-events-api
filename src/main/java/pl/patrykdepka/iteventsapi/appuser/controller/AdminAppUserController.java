package pl.patrykdepka.iteventsapi.appuser.controller;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.facade.CurrentUserFacade;
import pl.patrykdepka.iteventsapi.appuser.service.AdminAppUserService;

import javax.validation.Valid;
import java.util.Locale;

@Controller
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
    public String getAllUsers(@RequestParam(name = "page", required = false) Integer pageNumber,
                              @RequestParam(name = "sort_by", required = false) String sortProperty,
                              @RequestParam(name = "order_by", required = false) String sortDirection,
                              Model model) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
        String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "ASC";
        if (page > 0) {
            PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
            Page<AdminAppUserTableDTO> users = adminAppUserService.findAllUsers(pageRequest);
            if (page <= users.getTotalPages()) {
                model.addAttribute("users", users);
                model.addAttribute("prefixUrl", "/admin/users?");
                if (sortProperty != null) {
                    String sortParams = "sort_by=" + sortProperty + "&order_by=" + sortDirection;
                    model.addAttribute("sortParams", sortParams);
                }
                return "admin/app-user-table";
            } else {
                if ((sortProperty != null && !"".equals(sortProperty)) && (sortDirection != null && !"".equals(sortDirection))) {
                    return "redirect:/admin/users?page=" + users.getTotalPages() + "&sort_by=" + sortProperty + "&order_by=" + sortDirection;
                }
                return "redirect:/admin/users?page=" + users.getTotalPages();
            }
        } else {
            if ((sortProperty != null && !"".equals(sortProperty)) && (sortDirection != null && !"".equals(sortDirection))) {
                return "redirect:/admin/users?page=1" + "&sort_by=" + sortProperty + "&order_by=" + sortDirection;
            }
            return "redirect:/admin/users?page=1";
        }
    }

    @GetMapping("/admin/users/results")
    public String getUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                   @RequestParam(name = "page", required = false) Integer pageNumber,
                                   @RequestParam(name = "sort_by", required = false) String sortProperty,
                                   @RequestParam(name = "order_by", required = false) String sortDirection,
                                   Model model) {
        if (searchQuery != null) {
            int page = pageNumber != null ? pageNumber : 1;
            String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
            String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "ASC";
            if (page > 0) {
                PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
                Page<AdminAppUserTableDTO> users = adminAppUserService.findUsersBySearch(searchQuery, pageRequest);
                if (users.getNumberOfElements() == 0) {
                    model.addAttribute("users", users);
                    if (page > 1) {
                        if (sortProperty != null) {
                            return "redirect:/admin/users/results?search_query=" + searchQuery + "&sort_by=" + sortProperty;
                        }
                        return "redirect:/admin/users/results?search_query=" + searchQuery;
                    } else {
                        model.addAttribute("prefixUrl", "/admin/users/results?search_query=" + searchQuery + "&");
                        return "admin/app-user-table";
                    }
                } else if (page <= users.getTotalPages()) {
                    model.addAttribute("users", users);
                    searchQuery = searchQuery.replace(" ", "+");
                    model.addAttribute("searchQuery", searchQuery);
                    model.addAttribute("prefixUrl", "/admin/users/results?search_query=" + searchQuery + "&");
                    return "admin/app-user-table";
                } else {
                    searchQuery = searchQuery.replace(" ", "+");
                    if (sortProperty != null) {
                        return "redirect:/admin/users/results?search_query=" + searchQuery + "&page=" + users.getTotalPages() + "&sort_by=" + sortProperty;
                    }
                    return "redirect:/admin/users/results?search_query=" + searchQuery + "&page=" + users.getTotalPages();
                }
            } else {
                searchQuery = searchQuery.replace(" ", "+");
                if (sortProperty != null) {
                    return "redirect:/admin/users/results?search_query=" + searchQuery + "&page=1" + "&sort_by=" + sortProperty;
                }
                return "redirect:/admin/users/results?search_query=" + searchQuery + "&page=1";
            }
        } else {
            return "redirect:/admin/users/results?search_query=";
        }
    }

    @GetMapping("/admin/users/{id}/settings/account")
    public String showUserAccountEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountUpdated", false);
        model.addAttribute("userAccount", adminAppUserService.findUserAccountToEdit(id));
        return "admin/forms/app-user-account-edit-form";
    }

    @PatchMapping("/admin/users/{id}/settings/account")
    public String updateUserAccount(@PathVariable Long id,
                                    @Valid @ModelAttribute(name = "userAccount") AdminAppUserAccountEditDTO userAccount,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountUpdated", false);
        } else {
            model.addAttribute("userAccount", adminAppUserService.updateUserAccount(id, userAccount));
            model.addAttribute("accountUpdated", true);
        }
        return "admin/forms/app-user-account-edit-form";
    }

    @GetMapping("/admin/users/{id}/settings/profile")
    public String showUserProfileEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("profileUpdated", false);
        model.addAttribute("userProfile", adminAppUserService.findUserProfileToEdit(id));
        return "admin/forms/app-user-profile-edit-form";
    }

    @PatchMapping("/admin/users/{id}/settings/profile")
    public String updateUserProfile(@PathVariable Long id,
                                    @Valid @ModelAttribute(name = "userProfile") AdminAppUserProfileEditDTO userProfile,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("profileUpdated", false);
        } else {
            model.addAttribute("userProfile", adminAppUserService.updateUserProfile(id, userProfile));
            model.addAttribute("profileUpdated", true);
        }
        return "admin/forms/app-user-profile-edit-form";
    }

    @GetMapping("/admin/users/{id}/settings/password")
    public String showUserPasswordEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("passwordUpdated", false);
        model.addAttribute("newUserPassword", new AdminAppUserPasswordEditDTO(id));
        return "admin/forms/app-user-password-edit-form";
    }

    @PatchMapping("/admin/users/{id}/settings/password")
    public String updateUserPassword(@PathVariable Long id,
                                     @Valid @ModelAttribute(name = "newUserPassword") AdminAppUserPasswordEditDTO newUserPassword,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordUpdated", false);
            return "admin/forms/app-user-password-edit-form";
        } else {
            try {
                model.addAttribute("newUserPassword", adminAppUserService.updateUserPassword(currentUserFacade.getCurrentUser(), id, newUserPassword));
                model.addAttribute("passwordUpdated", true);
                return "admin/forms/app-user-password-edit-form";
            } catch (IncorrectCurrentPasswordException e) {
                bindingResult.addError(new FieldError("newUserPassword", "adminPassword", messageSource.getMessage("form.field.currentPassword.error.invalidCurrentPassword.message", null, Locale.getDefault())));
                model.addAttribute("passwordUpdated", false);
                return "admin/forms/app-user-password-edit-form";
            }
        }
    }

    @GetMapping("/admin/users/{id}/settings/delete_account")
    public String showUserDeleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("deleteUserData", new AdminDeleteAppUserDTO(id));
        return "admin/forms/app-user-delete-form";
    }

    @DeleteMapping("/admin/users/{id}")
    public String deleteUser(@PathVariable Long id,
                             @Valid @ModelAttribute(name = "deleteUserData") AdminDeleteAppUserDTO deleteUserData,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/forms/app-user-delete-form";
        } else {
            try {
                adminAppUserService.deleteUser(currentUserFacade.getCurrentUser(), deleteUserData);
                return "redirect:/admin/users";
            } catch (IncorrectCurrentPasswordException e) {
                bindingResult.addError(new FieldError("deleteUserData", "adminPassword", messageSource.getMessage("form.field.currentPassword.error.invalidCurrentPassword.message", null, Locale.getDefault())));
                return "admin/forms/app-user-delete-form";
            }
        }
    }
}
