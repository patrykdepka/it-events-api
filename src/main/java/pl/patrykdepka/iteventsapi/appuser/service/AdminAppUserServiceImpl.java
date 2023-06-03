package pl.patrykdepka.iteventsapi.appuser.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.mapper.AdminAppUserAccountEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.mapper.AdminAppUserProfileEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.mapper.AdminAppUserTableDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.appuser.repository.AppUserRepository;
import pl.patrykdepka.iteventsapi.appuser.specification.AppUserSpecification;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;
import pl.patrykdepka.iteventsapi.profileimage.service.ProfileImageService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AdminAppUserServiceImpl implements AdminAppUserService {
    private final AppUserRepository appUserRepository;
    private final ProfileImageService profileImageService;
    private final PasswordEncoder passwordEncoder;

    public AdminAppUserServiceImpl(
            AppUserRepository appUserRepository,
            ProfileImageService profileImageService,
            PasswordEncoder passwordEncoder
    ) {
        this.appUserRepository = appUserRepository;
        this.profileImageService = profileImageService;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<AdminAppUserTableDTO> findAllUsers(Pageable page) {
        return AdminAppUserTableDTOMapper.mapToAdminAppUserTableDTOs(appUserRepository.findAll(page));
    }

    public Page<AdminAppUserTableDTO> findUsersBySearch(String searchQuery, Pageable page) {
        searchQuery = searchQuery.toLowerCase();
        String[] searchWords = searchQuery.split(" ");

        if (searchWords.length == 1 && !"".equals(searchWords[0])) {
            return AdminAppUserTableDTOMapper
                    .mapToAdminAppUserTableDTOs(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0]), page));
        }
        if (searchWords.length == 2) {
            return AdminAppUserTableDTOMapper
                    .mapToAdminAppUserTableDTOs(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0], searchWords[1]), page));
        }

        return Page.empty();
    }

    public AdminAppUserAccountEditDTO findUserAccountToEdit(Long id) {
        return appUserRepository
                .findById(id)
                .map(AdminAppUserAccountEditDTOMapper::mapToAdminAppUserAccountEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AdminAppUserAccountEditDTO updateUserAccount(Long id, AdminAppUserAccountEditDTO userAccount) {
        return appUserRepository
                .findById(id)
                .map(target -> setUserAccountFields(userAccount, target))
                .map(AdminAppUserAccountEditDTOMapper::mapToAdminAppUserAccountEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    public AdminAppUserProfileEditDTO findUserProfileToEdit(Long id) {
        return appUserRepository
                .findById(id)
                .map(AdminAppUserProfileEditDTOMapper::mapToAdminAppUserProfileEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AdminAppUserProfileEditDTO updateUserProfile(Long id, AdminAppUserProfileEditDTO userProfile) {
        return appUserRepository
                .findById(id)
                .map(target -> setUserProfileFields(userProfile, target))
                .map(AdminAppUserProfileEditDTOMapper::mapToAdminAppUserProfileEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AdminAppUserPasswordEditDTO updateUserPassword(AppUser currentUser, Long id, AdminAppUserPasswordEditDTO newUserPassword) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, newUserPassword.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException();
        }

        appUserRepository
                .findById(id)
                .ifPresentOrElse(
                        user -> {
                            user.setPassword(passwordEncoder.encode(newUserPassword.getNewPassword()));
                        },
                        () -> {
                            throw new AppUserNotFoundException("User with ID " + id + " not found");
                        }
                );

        return new AdminAppUserPasswordEditDTO(id);
    }

    public void deleteUser(AppUser currentUser, AdminDeleteAppUserDTO deleteUserData) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, deleteUserData.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException();
        }

        if (!currentUser.getId().equals(deleteUserData.getId())) {
            appUserRepository.deleteById(deleteUserData.getId());
        }
    }

    private AppUser setUserAccountFields(AdminAppUserAccountEditDTO source, AppUser target) {
        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
        }
        if (!source.getRoles().equals(target.getRoles())) {
            target.setRoles(source.getRoles());
        }

        return target;
    }

    private AppUser setUserProfileFields(AdminAppUserProfileEditDTO source, AppUser target) {
        if (source.getProfileImage() != null && !source.getProfileImage().isEmpty()) {
            Optional<ProfileImage> profileImage = profileImageService.updateProfileImage(target, source.getProfileImage());
            profileImage.ifPresent(target::setProfileImage);
        }
        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
        }
        if (source.getDateOfBirth() != null && !source.getDateOfBirth().equals(target.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            target.setDateOfBirth(LocalDate.parse(source.getDateOfBirth(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
        }

        return target;
    }

    private boolean checkIfAdminPasswordIsCorrect(AppUser admin, String adminPassword) {
        return passwordEncoder.matches(adminPassword, admin.getPassword());
    }
}
