package io.github.pawelkorus.soidc;

import java.time.Instant;

/**
 * Interface representing basic user profile.
 *
 * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#StandardClaims">OpenID Connect Core 1.0</a>
 */
public interface UserProfileInfo {

    String getName();

    String getFamilyName();

    String getGivenName();

    String getMiddleName();

    String getNickname();

    String getPreferredUsername();

    String getProfile();

    String getPicture();

    String getWebsite();

    String getGender();

    String getBirthday();

    String getZoneInfo();

    String getLocale();

    Instant updatedAt();

}
