package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.*;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.AppUserNotFoundException;
import pl.patrykdepka.iteventsapi.appuser.domain.exception.IncorrectCurrentPasswordException;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AdminAppUserAccountEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AdminAppUserProfileEditDTOMapper;
import pl.patrykdepka.iteventsapi.appuser.domain.mapper.AdminAppUserTableDTOMapper;
import pl.patrykdepka.iteventsapi.image.domain.ImageService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    public AdminAppUserAccountEditDTO findUserAccountToEdit(Long id) {
        return appUserRepository.findById(id)
                .map(AdminAppUserAccountEditDTOMapper::mapToAdminAppUserAccountEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AdminAppUserAccountEditDTO updateUserAccount(AppUser currentUser, Long id, AdminAppUserAccountEditDTO userAccount) {
        Optional<AppUser> userOpt = appUserRepository.findById(id);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            if (setUserAccountFields(userAccount, user)) {
                logger.info("User [ID: " + user.getId() + "] account updated by user [ID: " + currentUser.getId() + "]");
            }

            return AdminAppUserAccountEditDTOMapper.mapToAdminAppUserAccountEditDTO(user);
        }

        throw new AppUserNotFoundException("User with ID " + id + " not found");
    }

    public AdminAppUserProfileEditDTO findUserProfileToEdit(Long id) {
        return appUserRepository.findById(id)
                .map(AdminAppUserProfileEditDTOMapper::mapToAdminAppUserProfileEditDTO)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AdminAppUserProfileEditDTO updateUserProfile(AppUser currentUser, Long id, AdminAppUserProfileEditDTO userProfile) {
        Optional<AppUser> userOpt = appUserRepository.findById(id);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            if (setUserProfileFields(userProfile, user)) {
                logger.info("User [ID: " + user.getId() + "] profile updated by user [ID: " + currentUser.getId() + "]");
            }

            return AdminAppUserProfileEditDTOMapper.mapToAdminAppUserProfileEditDTO(user);
        }

        throw new AppUserNotFoundException("User with ID " + id + " not found");
    }

    @Transactional
    public void updateUserPassword(AppUser currentUser, Long id, AdminAppUserPasswordEditDTO newUserPassword) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, newUserPassword.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException();
        }

        Optional<AppUser> userOpt = appUserRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new AppUserNotFoundException("User with ID " + id + " not found");
        }
        AppUser user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newUserPassword.getNewPassword()));
        logger.info("User [ID: " + user.getId() + "] password updated by user [ID: " + currentUser.getId() + "]");
    }

    public void deleteUser(AppUser currentUser, AdminDeleteAppUserDTO deleteUserData) {
        if (!checkIfAdminPasswordIsCorrect(currentUser, deleteUserData.getAdminPassword())) {
            throw new IncorrectCurrentPasswordException();
        }

        if (!currentUser.getId().equals(deleteUserData.getId())) {
            appUserRepository.deleteById(deleteUserData.getId());
            logger.info("User [ID: " + deleteUserData.getId() + "] deleted by user [ID: " + currentUser.getId() + "]");
        }
    }

    private boolean setUserAccountFields(AdminAppUserAccountEditDTO source, AppUser target) {
        boolean isUpdated = false;

        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
            isUpdated = true;
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
            isUpdated = true;
        }
        if (!source.getRoles().equals(target.getRoles())) {
            target.setRoles(source.getRoles());
            isUpdated = true;
        }

        return isUpdated;
    }

    private boolean setUserProfileFields(AdminAppUserProfileEditDTO source, AppUser target) {
        boolean isUpdated = false;

        if (source.getProfileImage() != null && !source.getProfileImage().isEmpty()) {
            imageService.updateImage(target.getProfileImage().getId(), source.getProfileImage());
//            Optional<Image> profileImage = imageService.updateImage(target.getProfileImage().getId(), source.getProfileImage());
//            if (profileImage.isPresent()) {
//                target.setProfileImage(profileImage.get());
//                isUpdated = true;
//            }
        }
        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
            isUpdated = true;
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
            isUpdated = true;
        }
        if (source.getDateOfBirth() != null && !source.getDateOfBirth().equals(target.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            target.setDateOfBirth(LocalDate.parse(source.getDateOfBirth(), DateTimeFormatter.ISO_LOCAL_DATE));
            isUpdated = true;
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
            isUpdated = true;
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
            isUpdated = true;
        }

        return isUpdated;
    }

    private boolean checkIfAdminPasswordIsCorrect(AppUser admin, String adminPassword) {
        return passwordEncoder.matches(adminPassword, admin.getPassword());
    }
}
