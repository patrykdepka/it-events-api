package pl.patrykdepka.iteventsapi.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import pl.patrykdepka.iteventsapi.image.domain.ImageType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class AppUserDetails extends User {
    private final ImageType profileImageType;
    private final String profileImageData;
    private final String firstName;
    private final String lastName;

    public AppUserDetails(AppUserDetailsBuilder appUserDetailsBuilder) {
        super(
                appUserDetailsBuilder.username,
                appUserDetailsBuilder.password,
                appUserDetailsBuilder.enabled,
                true,
                true,
                appUserDetailsBuilder.accountNonLocked,
                appUserDetailsBuilder.authorities
        );
        this.profileImageType = appUserDetailsBuilder.profileImageType;
        this.profileImageData = appUserDetailsBuilder.profileImageData;
        this.firstName = appUserDetailsBuilder.firstName;
        this.lastName = appUserDetailsBuilder.lastName;
    }

    public static class AppUserDetailsBuilder {
        private ImageType profileImageType;
        private String profileImageData;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private boolean enabled;
        private boolean accountNonLocked;
        private List<GrantedAuthority> authorities;

        public AppUserDetailsBuilder profileImageType(ImageType profileImageType) {
            this.profileImageType = profileImageType;
            return this;
        }

        public AppUserDetailsBuilder profileImageData(String profileImageData) {
            this.profileImageData = profileImageData;
            return this;
        }

        public AppUserDetailsBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserDetailsBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppUserDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AppUserDetailsBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserDetailsBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public AppUserDetails build() {
            return new AppUserDetails(this);
        }
    }
}
