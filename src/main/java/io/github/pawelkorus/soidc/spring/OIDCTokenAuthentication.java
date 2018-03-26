package io.github.pawelkorus.soidc.spring;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class OIDCTokenAuthentication implements Authentication {

    private String issuer;
    private String idToken;

    public OIDCTokenAuthentication(String issuer, String token) {
        this.issuer = issuer;
        this.idToken = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Can't change authenticated state for this object.");
    }

    @Override
    public String getName() {
        return "";
    }

    public String getIdToken() {
        return this.idToken;
    }

    public String getIssuer() {
        return this.issuer;
    }
}
