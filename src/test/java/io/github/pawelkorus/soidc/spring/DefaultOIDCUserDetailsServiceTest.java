package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.CommonClaim;
import io.github.pawelkorus.soidc.IdTokenPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    }

}
