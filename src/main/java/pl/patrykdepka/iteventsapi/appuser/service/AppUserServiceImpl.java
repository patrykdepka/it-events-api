package pl.patrykdepka.iteventsapi.appuser.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.dto.*;
import pl.patrykdepka.iteventsapi.appuser.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.mapper.AppUserProfileDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.mapper.AppUserProfileEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.mapper.AppUserTableDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.appuser.model.Role;
import pl.patrykdepka.iteventsapi.appuser.repository.AppUserRepository;
import pl.patrykdepka.iteventsapi.appuser.specification.AppUserSpecification;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;
import pl.patrykdepka.iteventsapi.profileimage.service.ProfileImageService;
import pl.patrykdepka.iteventsapi.security.AppUserDetailsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageService profileImageService;
    private final AppUserDetailsService appUserDetailsService;

    public AppUserServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            ProfileImageService profileImageService,
            AppUserDetailsService appUserDetailsService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileImageService = profileImageService;
        this.appUserDetailsService = appUserDetailsService;
    }

    public boolean checkIfUserExists(String email) {
        return appUserRepository.existsByEmail(email.toLowerCase());
    }

    @Transactional
    public void createUser(AppUserRegistrationDTO newUserData) {
        AppUser user = new AppUser();
        user.setProfileImage(profileImageService.createDefaultProfileImage());
        user.setFirstName(newUserData.getFirstName());
        user.setLastName(newUserData.getLastName());
        user.setDateOfBirth(LocalDate.parse(newUserData.getDateOfBirth(), DateTimeFormatter.ISO_LOCAL_DATE));
        user.setEmail(newUserData.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(newUserData.getPassword()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setRoles(List.of(Role.ROLE_USER));
        appUserRepository.save(user);
    }

    public AppUserProfileDTO findUserProfile(AppUser currentUser) {
        return AppUserProfileDTOMapper.mapToAppUserProfileDTO(currentUser);
    }

    public Page<AppUserTableDTO> findAllUsers(Pageable page) {
        return AppUserTableDTOMapper.mapToAppUserTableDTOs(appUserRepository.findAll(page));
    }

    public Page<AppUserTableDTO> findUsersBySearch(String searchQuery, Pageable page) {
        searchQuery = searchQuery.toLowerCase();
        String[] searchWords = searchQuery.split(" ");

        if (searchWords.length == 1 && !"".equals(searchWords[0])) {
            return AppUserTableDTOMapper
                    .mapToAppUserTableDTOs(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0]), page));
        }
        if (searchWords.length == 2) {
            return AppUserTableDTOMapper
                    .mapToAppUserTableDTOs(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0], searchWords[1]), page));
        }

        return Page.empty();
    }

    public AppUserProfileDTO findUserProfileByUserId(Long id) {
        return appUserRepository
                .findById(id)
                .map(AppUserProfileDTOMapper::mapToAppUserProfileDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    public AppUserProfileEditDTO findUserProfileToEdit(AppUser currentUser) {
        return AppUserProfileEditDTOMapper.mapToAppUserProfileEditDTO(currentUser);
    }

    @Transactional
    public AppUserProfileEditDTO updateUserProfile(AppUser currentUser, AppUserProfileEditDTO userProfile) {
        return AppUserProfileEditDTOMapper.mapToAppUserProfileEditDTO(setUserProfileFields(userProfile, currentUser));
    }

    @Transactional
    public AppUserPasswordEditDTO updateUserPassword(AppUser currentUser, AppUserPasswordEditDTO newUserPasswordData) {
        if (!checkIfCurrentPasswordIsCorrect(currentUser, newUserPasswordData.getCurrentPassword())) {
            throw new IncorrectCurrentPasswordException();
        }
        currentUser.setPassword(passwordEncoder.encode(newUserPasswordData.getNewPassword()));

        return new AppUserPasswordEditDTO();
    }

    private AppUser setUserProfileFields(AppUserProfileEditDTO source, AppUser target) {
        if (source.getProfileImage() != null && !source.getProfileImage().isEmpty()) {
            Optional<ProfileImage> profileImage = profileImageService.updateProfileImage(target, source.getProfileImage());
            if (profileImage.isPresent()) {
                target.setProfileImage(profileImage.get());
                appUserDetailsService.updateAppUserDetails(target);
            }
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
        }

        return target;
    }

    private boolean checkIfCurrentPasswordIsCorrect(AppUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
