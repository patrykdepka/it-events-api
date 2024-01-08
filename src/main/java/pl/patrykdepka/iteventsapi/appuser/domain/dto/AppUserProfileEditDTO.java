package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.core.Image;

import javax.validation.constraints.Size;

@Getter
@Setter
public class AppUserProfileEditDTO {
    @Image(width = 250, height = 250)
    private MultipartFile profileImage;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    private String city;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    private String bio;

    private AppUserProfileEditDTO() {
    }

    public static AppUserProfileEditDTOBuilder builder() {
        return new AppUserProfileEditDTOBuilder();
    }

    public static class AppUserProfileEditDTOBuilder {
        private MultipartFile profileImage;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String city;
        private String bio;

        public AppUserProfileEditDTOBuilder profileImage(MultipartFile profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AppUserProfileEditDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileEditDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileEditDTOBuilder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public AppUserProfileEditDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileEditDTOBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileEditDTO build() {
            AppUserProfileEditDTO userProfile = new AppUserProfileEditDTO();
            userProfile.setProfileImage(profileImage);
            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);
            userProfile.setDateOfBirth(dateOfBirth);
            userProfile.setCity(city);
            userProfile.setBio(bio);
            return userProfile;
        }
    }
}
