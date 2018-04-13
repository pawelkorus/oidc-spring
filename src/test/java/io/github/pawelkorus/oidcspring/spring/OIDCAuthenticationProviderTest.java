package io.github.pawelkorus.oidcspring.spring;

import io.github.pawelkorus.oidcspring.IdTokenPayload;
import io.github.pawelkorus.oidcspring.IdentityProviderConfig;
import io.github.pawelkorus.oidcspring.process.TokenProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OIDCAuthenticationProviderTest {

    private String TEST_ID_TOKEN = "TEST_ID_TOKEN";
    private String TEST_ISSUER = "TEST_ISS";

    @Mock
    IdentityProviderConfig identityProviderData;

    @Mock
    OIDCUserDetailsService oidcUserDetailsService;

    @Mock
    UserDetails userDetails;

    @Mock
    TokenProcessor tokenProcessor;

    @Mock
    OIDCTokenAuthentication authentication;

    @Mock
    IdTokenPayload idTokenPayload;

    @InjectMocks
    OIDCAuthenticationProvider authenticationProvider;

    @Before
    public void setup() throws Exception {
        when(authentication.getIdToken()).thenReturn(TEST_ID_TOKEN);
        when(authentication.getIssuer()).thenReturn(TEST_ISSUER);

        when(identityProviderData.getIssuer()).thenReturn(TEST_ISSUER);

        when(tokenProcessor.verifyAndDecode(TEST_ID_TOKEN)).thenReturn(idTokenPayload);

        when(oidcUserDetailsService.loadUser(idTokenPayload)).thenReturn(userDetails);
    }

    @Test
    public void should_authenticate() throws Exception {
        VerifiedOIDCTokenAuthentication verifiedOIDCTokenAuthentication = (VerifiedOIDCTokenAuthentication) authenticationProvider.authenticate(authentication);

        assertThat(verifiedOIDCTokenAuthentication).isNotNull();
        assertThat(verifiedOIDCTokenAuthentication.isAuthenticated()).isTrue();
        assertThat(verifiedOIDCTokenAuthentication.getDetails()).isEqualTo(userDetails);
        assertThat(verifiedOIDCTokenAuthentication.getPrincipal()).isEqualTo(userDetails);
        verify(tokenProcessor).verifyAndDecode(TEST_ID_TOKEN);
        verify(oidcUserDetailsService).loadUser(idTokenPayload);
    }

    @Test
    public void should_return_null_if_issuer_doesnt_match() throws Exception {
        when(authentication.getIssuer()).thenReturn("xxx");

        assertThat(authenticationProvider.authenticate(authentication)).isNull();
    }
}
