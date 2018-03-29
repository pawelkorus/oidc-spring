package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerifiedOIDCAuthenticationTokenTest {

    private static final String SAMPLE_ISSUER = "sampleIssuer";
    private static final String SAMPLE_TOKEN = "sampleToken";
    private static final String SAMPLE_USER_NAME = "sampleUserName";
    private static final SimpleGrantedAuthority SAMPLE_AUTHORITY = new SimpleGrantedAuthority("ROLE_USER");

    @Mock
    private UserDetails userDetails;

    private VerifiedOIDCTokenAuthentication verifiedOIDCTokenAuthentication;

    @Before
    public void setup() {
        when(userDetails.getUsername()).thenReturn(SAMPLE_USER_NAME);
        when(userDetails.getAuthorities()).then(TestUtils.setupListAnswer(SAMPLE_AUTHORITY));

        verifiedOIDCTokenAuthentication = new VerifiedOIDCTokenAuthentication(SAMPLE_ISSUER, SAMPLE_TOKEN, userDetails);
    }

    @Test
    public void is_authenticated_method_should_return_true() {
        assertThat(verifiedOIDCTokenAuthentication.isAuthenticated()).isTrue();
    }

    @Test
    public void authorities_should_be_taken_from_user_details() {
        assertThat(verifiedOIDCTokenAuthentication.getAuthorities()).contains(SAMPLE_AUTHORITY);
        verify(userDetails).getAuthorities();
    }

    @Test
    public void user_details_should_be_used_as_principal() {
        assertThat(verifiedOIDCTokenAuthentication.getPrincipal()).isEqualTo(userDetails);
        assertThat(verifiedOIDCTokenAuthentication.getDetails()).isEqualTo(userDetails);
    }

    @Test
    public void get_name_should_delegate_to_user_details() {
        assertThat(verifiedOIDCTokenAuthentication.getName()).isEqualTo(SAMPLE_USER_NAME);
        verify(userDetails).getUsername();
    }

}
