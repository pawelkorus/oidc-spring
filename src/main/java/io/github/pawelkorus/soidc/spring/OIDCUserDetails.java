package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.UserEmailInfo;
import io.github.pawelkorus.soidc.UserProfileInfo;
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
        return null;
    }

    @Override
    public String getPreferredUsername() {
        return null;
    }

    @Override
    public String getProfile() {
        return null;
    }

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public String getWebsite() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getZoneInfo() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public Instant updatedAt() {
        return null;
    }

    public static class OIDCUserDetailsBuilder {
        private String sub;
        private String email;
        private boolean emailVerified = false;
        private String name;
        private String givenName;
        private String middleName;
        private String familyName;

        public OIDCUserDetailsBuilder() {
        }

        public OIDCUserDetailsBuilder sub(String v) {
            this.sub = v;
            return this;
        }

        public OIDCUserDetailsBuilder email(String email, boolean verified) {
            this.email = email;
            this.emailVerified = verified;
            return this;
        }

        public OIDCUserDetailsBuilder names(String name, String givenName, String middleName, String familyName) {
            this.name = name;
            this.givenName = givenName;
            this.middleName = middleName;
            this.familyName = familyName;
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
            return oidcUserDetails;
        }
    }
}
