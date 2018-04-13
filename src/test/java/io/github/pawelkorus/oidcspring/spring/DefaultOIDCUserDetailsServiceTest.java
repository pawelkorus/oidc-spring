package io.github.pawelkorus.oidcspring.spring;

import io.github.pawelkorus.oidcspring.CommonClaim;
import io.github.pawelkorus.oidcspring.IdTokenPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOIDCUserDetailsServiceTest {

    private static String SAMPLE_SUB = "sampleSub";

    @Mock
    private IdTokenPayload idTokenPayload;

    private DefaultOIDCUserDetailsService defaultOIDCUserDetailsService;

    @Before
    public void setup() {
        defaultOIDCUserDetailsService = new DefaultOIDCUserDetailsService();
    }

    @Test
    public void should_create_user_details_from_id_token_payload() {
        when(idTokenPayload.getAsString(CommonClaim.sub.name())).thenReturn(SAMPLE_SUB);

        OIDCUserDetails userDetails = defaultOIDCUserDetailsService.loadUser(idTokenPayload);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(SAMPLE_SUB);
        assertThat(userDetails.getPassword()).isNullOrEmpty();
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)).contains("ROLE_USER");
        assertThat(userDetails.isEnabled()).isTrue();

        Stream.of(CommonClaim.sub, CommonClaim.name, CommonClaim.email, CommonClaim.birthday,
                CommonClaim.gender, CommonClaim.locale, CommonClaim.family_name, CommonClaim.given_name,
                CommonClaim.middle_name, CommonClaim.nickname, CommonClaim.picture, CommonClaim.preferred_username,
                CommonClaim.profile, CommonClaim.website, CommonClaim.zoneinfo)
                .forEach(claim -> {
                    verify(idTokenPayload).getAsString(claim.name());
                });
        verify(idTokenPayload).getAsInstant(CommonClaim.updated_at.name());
        verify(idTokenPayload).getAsBoolean(CommonClaim.email_verified.name());
        verifyNoMoreInteractions(idTokenPayload);
    }

}
