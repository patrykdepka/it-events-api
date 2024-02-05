package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserPasswordEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserTableDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminDeleteAppUserDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AdminAppUserAccountDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AdminAppUserTableDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AppUserProfileEditDTOMapper;
import pl.patrykdepka.iteventsapi.image.domain.ImageService;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Service
@RequiredArgsConstructor
public class AdminAppUserService {
    private final Logger logger = LoggerFactory.getLogger(AdminAppUserService.class);
    private final AppUserRepository appUserRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

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

    public AdminAppUserAccountEditDTO findUserAccountData(Long id) {
        return appUserRepository.findById(id)
                .map(AdminAppUserAccountDTOMapper::mapToAdminAppUserAccountDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
    }

    public AdminAppUserAccountEditDTO updateUserAccountData(AppUser currentUser, Long id, AdminAppUserAccountEditDTO newAccountData) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
        if (newAccountData.isEnabled() != user.isEnabled()) {
            user.setEnabled(newAccountData.isEnabled());
        }
        if (newAccountData.isAccountNonLocked() != user.isAccountNonLocked()) {
            user.setAccountNonLocked(newAccountData.isAccountNonLocked());
        }
        if (!newAccountData.getRoles().equals(user.getRoles())) {
            user.setRoles(newAccountData.getRoles());
        }
        logger.info("User [ID: {}] account was updated by user [ID: {}]", user.getId(), currentUser.getId());
        return AdminAppUserAccountDTOMapper.mapToAdminAppUserAccountDTO(user);
    }

    public AppUserProfileEditDTO findUserProfileData(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileEditDTOMapper::mapToAppUserProfileEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
    }

    public AppUserProfileEditDTO updateUserProfileData(AppUser currentUser, Long id, AppUserProfileEditDTO newProfileData) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
        if (newProfileData.getProfileImage() != null) {
            imageService.updateImage(user.getProfileImage().getId(), newProfileData.getProfileImage());
        }
        if (newProfileData.getFirstName() != null && !newProfileData.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(newProfileData.getFirstName());
        }
        if (newProfileData.getLastName() != null && !newProfileData.getLastName().equals(user.getLastName())) {
            user.setLastName(newProfileData.getLastName());
        }
        if (newProfileData.getDateOfBirth() != null && !newProfileData.getDateOfBirth().equals(user.getDateOfBirth().format(ISO_LOCAL_DATE))) {
            user.setDateOfBirth(LocalDate.parse(newProfileData.getDateOfBirth(), ISO_LOCAL_DATE));
        }
        if (newProfileData.getCity() != null && !newProfileData.getCity().equals(user.getCity())) {
            user.setCity(newProfileData.getCity());
        }
        if (newProfileData.getBio() != null && !newProfileData.getBio().equals(user.getBio())) {
            user.setBio(newProfileData.getBio());
        }
        logger.info("User [ID: {}] profile was updated by user [ID: {}]", user.getId(), currentUser.getId());
        return AppUserProfileEditDTOMapper.mapToAppUserProfileEditDTO(user);
    }

    public void updateUserPassword(AppUser currentUser, Long id, AdminAppUserPasswordEditDTO newUserPassword) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, newUserPassword.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException("adminPassword");
        }
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException("User [ID: " + id + "] not found"));
        user.setPassword(passwordEncoder.encode(newUserPassword.getNewPassword()));
        appUserRepository.save(user);
        logger.info("User [ID: {}] password was updated by user [ID: {}]", user.getId(), currentUser.getId());
    }

    public void deleteUser(AppUser currentUser, Long id, AdminDeleteAppUserDTO deleteUserData) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, deleteUserData.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException("adminPassword");
        }
        if (!currentUser.getId().equals(id)) {
            try {
                appUserRepository.deleteById(id);
                logger.info("User [ID: {}] was deleted by user [ID: {}]", id, currentUser.getId());
            } catch (EmptyResultDataAccessException ignored) {}
        }
    }

    private boolean checkIfAdminPasswordIsCorrect(AppUser admin, String adminPassword) {
        return passwordEncoder.matches(adminPassword, admin.getPassword());
    }
}
