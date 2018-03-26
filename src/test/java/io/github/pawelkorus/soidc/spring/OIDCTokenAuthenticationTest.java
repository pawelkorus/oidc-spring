package io.github.pawelkorus.soidc.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OIDCTokenAuthenticationTest {

    private String TEST_ID_TOKEN = "TEST_ID_TOKEN";
    private String TEST_ISSUER = "TEST_ISS";

    @Test
    public void should_return_false_if_is_authenticated_method_is_called() {
        Authentication authentication = new OIDCTokenAuthentication(TEST_ISSUER, TEST_ID_TOKEN);

        assertThat(authentication.isAuthenticated()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exeption_when_trying_to_change_is_authenticated_status() {
        Authentication authentication = new OIDCTokenAuthentication(TEST_ISSUER, TEST_ID_TOKEN);

        authentication.setAuthenticated(true);
    }

}
