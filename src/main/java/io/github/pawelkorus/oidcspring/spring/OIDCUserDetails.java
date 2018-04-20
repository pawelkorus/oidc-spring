package io.github.pawelkorus.oidcspring.spring;

import io.github.pawelkorus.oidcspring.UserEmailInfo;
import io.github.pawelkorus.oidcspring.UserProfileInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

public class OIDCUserDetails implements UserDetails, UserEmailInfo, UserProfileInfo {

    private String userId;
    private String email;
    private String name;
    private String givenName;
    private String middleName;
    private String familyName;
    private boolean emailVerified;
    private String nickName;
    private String preferredUsername;
    private String profileURL;
    private String pictureURL;
    private String websiteURL;
    private String gender;
    private String birthdayYYYYYMMDD;
    private String zoneInfo;
    private String locale;
    private Instant updatedAt;

    public OIDCUserDetails(String sub) {
        this.userId = sub;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email == null ? this.userId : this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public static OIDCUserDetailsBuilder builder() {
        return new OIDCUserDetailsBuilder();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getFamilyName() {
        return this.familyName;
    }

    @Override
    public String getGivenName() {
        return this.givenName;
    }

    @Override
    public String getMiddleName() {
        return this.middleName;
    }

    @Override
    public String getNickname() {
        return nickName;
    }

    @Override
    public String getPreferredUsername() {
        return preferredUsername;
    }

    @Override
    public String getProfile() {
        return profileURL;
    }

    @Override
    public String getPicture() {
        return pictureURL;
    }

    @Override
    public String getWebsite() {
        return websiteURL;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getBirthday() {
        return birthdayYYYYYMMDD;
    }

    @Override
    public String getZoneInfo() {
        return zoneInfo;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public Instant updatedAt() {
        return updatedAt;
    }

    public static class OIDCUserDetailsBuilder {
        private String sub;
        private String email;
        private boolean emailVerified = false;
        private String name;
        private String givenName;
        private String middleName;
        private String familyName;
        private String nickName;
        private String preferredUsername;
        private String birthday;
        private String zoneInfo;
        private String locale;
        private String profileURL;
        private String pictureURL;
        private String websiteURL;
        private String gender;
        private Instant updatedAt;

        public OIDCUserDetailsBuilder() {
        }

        public OIDCUserDetailsBuilder sub(String v) {
            this.sub = v;
            return this;
        }

        public OIDCUserDetailsBuilder email(String email, Boolean verified) {
            this.email = email;
            this.emailVerified = verified == null ? false : verified;
            return this;
        }

        public OIDCUserDetailsBuilder names(String name, String givenName, String middleName, String familyName, String nickName) {
            this.name = name;
            this.givenName = givenName;
            this.middleName = middleName;
            this.familyName = familyName;
            this.nickName = nickName;
            return this;
        }

        public OIDCUserDetailsBuilder preferredUsername(String preferredUsername) {
            this.preferredUsername = preferredUsername;
            return this;
        }

        public OIDCUserDetailsBuilder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public OIDCUserDetailsBuilder zoneInfo(String zoneInfo) {
            this.zoneInfo = zoneInfo;
            return this;
        }

        public OIDCUserDetailsBuilder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public OIDCUserDetailsBuilder urls(String pictureURL, String profileURL, String websiteURL) {
            this.pictureURL = pictureURL;
            this.profileURL = profileURL;
            this.websiteURL = websiteURL;
            return this;
        }

        public OIDCUserDetailsBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public OIDCUserDetailsBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OIDCUserDetails build() {
            OIDCUserDetails oidcUserDetails = new OIDCUserDetails(this.sub);
            oidcUserDetails.email = this.email;
            oidcUserDetails.emailVerified = this.emailVerified;
            oidcUserDetails.name = this.name;
            oidcUserDetails.givenName = this.givenName;
            oidcUserDetails.middleName = this.middleName;
            oidcUserDetails.familyName = this.familyName;
            oidcUserDetails.nickName = this.nickName;
            oidcUserDetails.preferredUsername = this.preferredUsername;
            oidcUserDetails.birthdayYYYYYMMDD = this.birthday;
            oidcUserDetails.updatedAt = this.updatedAt;
            oidcUserDetails.zoneInfo = this.zoneInfo;
            oidcUserDetails.locale = this.locale;
            oidcUserDetails.profileURL = this.profileURL;
            oidcUserDetails.pictureURL = this.pictureURL;
            oidcUserDetails.websiteURL = this.websiteURL;
            oidcUserDetails.gender = this.gender;
            return oidcUserDetails;
        }
    }
}
