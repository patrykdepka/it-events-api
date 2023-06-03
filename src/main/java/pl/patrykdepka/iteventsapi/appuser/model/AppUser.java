package pl.patrykdepka.iteventsapi.appuser.model;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.core.BaseEntity;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class AppUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "app_user_profile_image",
            joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
    )
    private ProfileImage profileImage;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String city;
    private String bio;
    private String email;
    private String password;
    private boolean enabled;
    private boolean accountNonLocked;
    @ElementCollection
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public static AppUserBuilder builder() {
        return new AppUserBuilder();
    }

    public static class AppUserBuilder {
        private Long id;
        private ProfileImage profileImage;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String city;
        private String bio;
        private String email;
        private String password;
        private boolean enabled;
        private boolean accountNonLocked;
        private List<Role> roles;

        public AppUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AppUserBuilder profileImage(ProfileImage profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AppUserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public AppUserBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AppUserBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AppUserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserBuilder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public AppUser build() {
            AppUser user = new AppUser();
            user.setId(id);
            user.setProfileImage(profileImage);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setDateOfBirth(dateOfBirth);
            user.setCity(city);
            user.setBio(bio);
            user.setEmail(email);
            user.setPassword(password);
            user.setEnabled(enabled);
            user.setAccountNonLocked(accountNonLocked);
            user.setRoles(roles);
            return user;
        }
    }
}
