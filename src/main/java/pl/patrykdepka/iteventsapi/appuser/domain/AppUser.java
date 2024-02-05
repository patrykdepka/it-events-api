package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.core.BaseEntity;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    private Image profileImage;
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
        private String uuid;
        private Image profileImage;
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

        public AppUserBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public AppUserBuilder profileImage(Image profileImage) {
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
            user.setUuid(uuid);
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
