package io.github.pawelkorus.soidc.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class OIDCUserDetailsTest {

    private static String SAMPLE_EMAIL = "sampleEmail@domain.com";
    private static String SAMPLE_SUB = "sampleSub";
    private static Instant SAMPLE_INSTANT = Instant.now();

    @Test
    public void should_build_user_details() {
        OIDCUserDetails oidcUserDetails = OIDCUserDetails.builder()
                .sub(SAMPLE_SUB)
                .email(SAMPLE_EMAIL, true)
                .names("sampleName", "sampleGivenName", "sampleMiddleName", "sampleFamilyName", "sampleNickName")
                .preferredUsername("samplePreferredUsername")
                .birthday("sampleBirthday")
                .locale("sampleLocale")
                .zoneInfo("sampleZoneInfo")
                .urls("samplePictureUrl", "sampleProfileUrl", "sampleWebsiteUrl")
                .gender("sampleGender")
                .updatedAt(SAMPLE_INSTANT)
                .build();

        assertThat(oidcUserDetails.getPassword()).isNull();
        assertThat(oidcUserDetails.getUsername()).isEqualTo(SAMPLE_EMAIL);
        assertThat(oidcUserDetails.getEmail()).isEqualTo(SAMPLE_EMAIL);
        assertThat(oidcUserDetails.isEmailVerified()).isTrue();
        assertThat(oidcUserDetails.isEnabled()).isTrue();
        assertThat(oidcUserDetails.isAccountNonExpired()).isTrue();
        assertThat(oidcUserDetails.isAccountNonLocked()).isTrue();
        assertThat(oidcUserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(oidcUserDetails.getUserId()).isEqualTo(SAMPLE_SUB);
        assertThat(oidcUserDetails.getName()).isEqualTo("sampleName");
        assertThat(oidcUserDetails.getGivenName()).isEqualTo("sampleGivenName");
        assertThat(oidcUserDetails.getMiddleName()).isEqualTo("sampleMiddleName");
        assertThat(oidcUserDetails.getFamilyName()).isEqualTo("sampleFamilyName");
        assertThat(oidcUserDetails.getNickname()).isEqualTo("sampleNickName");
        assertThat(oidcUserDetails.getPreferredUsername()).isEqualTo("samplePreferredUsername");
        assertThat(oidcUserDetails.getBirthday()).isEqualTo("sampleBirthday");
        assertThat(oidcUserDetails.getLocale()).isEqualTo("sampleLocale");
        assertThat(oidcUserDetails.getZoneInfo()).isEqualTo("sampleZoneInfo");
        assertThat(oidcUserDetails.getProfile()).isEqualTo("sampleProfileUrl");
        assertThat(oidcUserDetails.getPicture()).isEqualTo("samplePictureUrl");
        assertThat(oidcUserDetails.getWebsite()).isEqualTo("sampleWebsiteUrl");
        assertThat(oidcUserDetails.getGender()).isEqualTo("sampleGender");
        assertThat(oidcUserDetails.updatedAt()).isEqualTo(SAMPLE_INSTANT);
    }


    @Test
    public void when_calling_get_username_should_return_user_id_when_email_not_present() {
        assertThat(OIDCUserDetails.builder().sub(SAMPLE_SUB).build().getUsername()).isEqualTo(SAMPLE_SUB);
    }


}
