package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Value;
import pl.patrykdepka.iteventsapi.core.Image;
import pl.patrykdepka.iteventsapi.image.domain.dto.ImageDTO;

import javax.validation.constraints.Size;

@Value
public class AppUserProfileEditDTO {
    @Image(width = 250, height = 250)
    ImageDTO profileImage;
    String firstName;
    String lastName;
    String dateOfBirth;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    String city;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    String bio;

    public static AppUserProfileEditDTOBuilder builder() {
        return new AppUserProfileEditDTOBuilder();
    }

    public static class AppUserProfileEditDTOBuilder {
        private ImageDTO profileImage;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String city;
        private String bio;

        public AppUserProfileEditDTOBuilder profileImage(ImageDTO profileImage) {
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
            return new AppUserProfileEditDTO(
                    profileImage,
                    firstName,
                    lastName,
                    dateOfBirth,
                    city,
                    bio
            );
        }
    }
}
