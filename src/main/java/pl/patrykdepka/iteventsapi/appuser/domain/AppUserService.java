package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserTableDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.NewUserDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AppUserProfileDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AppUserProfileEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AppUserTableDTOMapper;
import pl.patrykdepka.iteventsapi.image.domain.ImageService;
import pl.patrykdepka.iteventsapi.security.AppUserDetailsService;

import java.time.LocalDate;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static pl.patrykdepka.iteventsapi.appuser.domain.Role.ROLE_USER;
import static pl.patrykdepka.iteventsapi.image.domain.ImageService.DEFAULT_PROFILE_IMAGE_NAME;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final Logger logger = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final AppUserDetailsService appUserDetailsService;

    public boolean checkIfUserExists(String email) {
        return appUserRepository.existsByEmail(email.toLowerCase());
    }

    public AppUserProfileDTO createUser(NewUserDTO newUserData) {
        AppUser user = new AppUser();
        user.setProfileImage(imageService.createDefaultImage(DEFAULT_PROFILE_IMAGE_NAME, PROFILE_IMAGE));
        user.setFirstName(newUserData.getFirstName());
        user.setLastName(newUserData.getLastName());
        user.setDateOfBirth(LocalDate.parse(newUserData.getDateOfBirth(), ISO_LOCAL_DATE));
        user.setEmail(newUserData.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(newUserData.getPassword()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setRoles(List.of(ROLE_USER));
        AppUser createdUser = appUserRepository.save(user);
        logger.info("User [ID: {}] created", createdUser.getId());
        return AppUserProfileDTOMapper.mapToAppUserProfileDTO(createdUser);
    }

    public AppUserProfileDTO findUserProfileById(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileDTOMapper::mapToAppUserProfileDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
    }

    public Page<AppUserTableDTO> findAllUsers(Pageable page) {
        return AppUserTableDTOMapper.mapToAppUserTableDTOs(appUserRepository.findAll(page));
    }

    public Page<AppUserTableDTO> findUsersBySearch(String searchQuery, Pageable page) {
        searchQuery = searchQuery.toLowerCase();
        String[] searchWords = searchQuery.split(" ");

        if (searchWords.length == 1 && !"".equals(searchWords[0])) {
            return AppUserTableDTOMapper.mapToAppUserTableDTOs(
                    appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0]), page)
            );
        }
        if (searchWords.length == 2) {
            return AppUserTableDTOMapper.mapToAppUserTableDTOs(
                    appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0], searchWords[1]), page)
            );
        }

        return Page.empty();
    }

    public AppUserProfileDTO findUserProfile(AppUser user) {
        return AppUserProfileDTOMapper.mapToAppUserProfileDTO(user);
    }

    public AppUserProfileEditDTO findUserProfileData(AppUser user) {
        return AppUserProfileEditDTOMapper.mapToAppUserProfileEditDTO(user);
    }

    public AppUserProfileEditDTO updateUserProfileData(AppUser user, AppUserProfileEditDTO newProfileData) {
        if (newProfileData.getProfileImage() != null) {
            imageService.updateImage(user.getProfileImage().getId(), newProfileData.getProfileImage());
            appUserDetailsService.updateAppUserDetails(user);
        }
        if (newProfileData.getCity() != null && !newProfileData.getCity().equals(user.getCity())) { // city może być puste, przy equals może polecieć NullPointerException
            user.setCity(newProfileData.getCity());
        }
        if (newProfileData.getBio() != null && !newProfileData.getBio().equals(user.getBio())) { // bio może być puste, przy equals może polecieć NullPointerException
            user.setBio(newProfileData.getBio());
        }

        return AppUserProfileEditDTOMapper.mapToAppUserProfileEditDTO(user);
    }

    @Transactional
    public void updateUserPassword(AppUser user, AppUserPasswordEditDTO newPasswordData) {
        if (!checkIfCurrentPasswordIsCorrect(user, newPasswordData.getCurrentPassword())) {
            throw new IncorrectCurrentPasswordException("currentPassword");
        }
        user.setPassword(passwordEncoder.encode(newPasswordData.getNewPassword()));
        logger.info("User [ID: {}] updated his password", user.getId());
    }

    private boolean checkIfCurrentPasswordIsCorrect(AppUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
