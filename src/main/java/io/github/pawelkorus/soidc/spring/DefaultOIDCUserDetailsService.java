package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.CommonClaim;
import io.github.pawelkorus.soidc.IdTokenPayload;

public class DefaultOIDCUserDetailsService implements OIDCUserDetailsService {
    @Override
    public OIDCUserDetails loadUser(IdTokenPayload idTokenPayload) {
        return OIDCUserDetails.builder()
                .sub(idTokenPayload.getAsString(CommonClaim.sub.name()))
                .gender(idTokenPayload.getAsString(CommonClaim.gender.name()))
                .urls(idTokenPayload.getAsString(CommonClaim.picture.name()),
                        idTokenPayload.getAsString(CommonClaim.profile.name()),
                        idTokenPayload.getAsString(CommonClaim.website.name()))
                .names(idTokenPayload.getAsString(CommonClaim.name.name()),
                        idTokenPayload.getAsString(CommonClaim.given_name.name()),
                        idTokenPayload.getAsString(CommonClaim.middle_name.name()),
                        idTokenPayload.getAsString(CommonClaim.family_name.name()),
                        idTokenPayload.getAsString(CommonClaim.nickname.name()))
                .zoneInfo(idTokenPayload.getAsString(CommonClaim.zoneinfo.name()))
                .locale(idTokenPayload.getAsString(CommonClaim.locale.name()))
                .birthday(idTokenPayload.getAsString(CommonClaim.birthday.name()))
                .preferredUsername(idTokenPayload.getAsString(CommonClaim.preferred_username.name()))
                .updatedAt(idTokenPayload.getAsInstant(CommonClaim.updated_at.name()))
                .email(idTokenPayload.getAsString(CommonClaim.email.name()),
                        idTokenPayload.getAsBoolean(CommonClaim.email_verified.name()))
                .build();
    }
}
