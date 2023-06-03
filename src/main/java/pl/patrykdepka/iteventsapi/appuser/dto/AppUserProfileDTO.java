package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserProfileDTO {
    private String profileImageType;
    private String profileImageData;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String city;
    private String bio;

    private AppUserProfileDTO() {
    }

    public static AppUserProfileDTOBuilder builder() {
        return new AppUserProfileDTOBuilder();
    }

    public static class AppUserProfileDTOBuilder {
        private String profileImageType;
        private String profileImageData;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String city;
        private String bio;

        public AppUserProfileDTOBuilder profileImageType(String profileImageType) {
            this.profileImageType = profileImageType;
            return this;
        }

        public AppUserProfileDTOBuilder profileImageData(String profileImageData) {
            this.profileImageData = profileImageData;
            return this;
        }

        public AppUserProfileDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileDTOBuilder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public AppUserProfileDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileDTOBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileDTO build() {
            AppUserProfileDTO userProfile = new AppUserProfileDTO();
            userProfile.setProfileImageType(profileImageType);
            userProfile.setProfileImageData(profileImageData);
            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);
            userProfile.setDateOfBirth(dateOfBirth);
            userProfile.setCity(city);
            userProfile.setBio(bio);
            return userProfile;
        }
    }
}
