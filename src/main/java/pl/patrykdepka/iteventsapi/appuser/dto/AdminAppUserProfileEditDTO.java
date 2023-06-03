package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.core.Image;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AdminAppUserProfileEditDTO {
    private Long id;
    private String profileImageType;
    private String profileImageData;
    @Image(width = 250, height = 250)
    private MultipartFile profileImage;
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @NotEmpty(message = "{form.field.firstName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @NotEmpty(message = "{form.field.lastName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @NotNull(message = "{form.field.dateOfBirth.error.notNull.message}")
    @NotEmpty(message = "{form.field.dateOfBirth.error.notEmpty.message}")
    private String dateOfBirth;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    private String city;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    private String bio;

    private AdminAppUserProfileEditDTO() {
    }

    public static AdminAppUserProfileEditDTOBuilder builder() {
        return new AdminAppUserProfileEditDTOBuilder();
    }

    public static class AdminAppUserProfileEditDTOBuilder {
        private Long id;
        private String profileImageType;
        private String profileImageData;
        private MultipartFile profileImage;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String city;
        private String bio;

        public AdminAppUserProfileEditDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder profileImageType(String profileImageType) {
            this.profileImageType = profileImageType;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder profileImageData(String profileImageData) {
            this.profileImageData = profileImageData;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder profileImage(MultipartFile profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public AdminAppUserProfileEditDTOBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public AdminAppUserProfileEditDTO build() {
            AdminAppUserProfileEditDTO userProfile = new AdminAppUserProfileEditDTO();
            userProfile.setId(id);
            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);
            userProfile.setProfileImageType(profileImageType);
            userProfile.setProfileImageData(profileImageData);
            userProfile.setProfileImage(profileImage);
            userProfile.setDateOfBirth(dateOfBirth);
            userProfile.setCity(city);
            userProfile.setBio(bio);
            return userProfile;
        }
    }
}
