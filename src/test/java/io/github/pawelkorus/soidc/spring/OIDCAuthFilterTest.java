package io.github.pawelkorus.soidc.spring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OIDCAuthFilterTest {

    private static String SAMPLE_TOKEN = "sampletoken";

    @Mock
    private RequestMatcher requestMatcher;

    @Mock
    private OAuth2RestOperations restTemplate;

    @Mock
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

    @Mock
    private OAuth2AccessToken oAuth2AccessToken;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private OIDCAuthFilter oidcAuthFilter;

    @Before
    public void setup() {
        when(restTemplate.getAccessToken()).thenReturn(oAuth2AccessToken);
        when(restTemplate.getResource()).thenReturn(oAuth2ProtectedResourceDetails);

        when(oAuth2AccessToken.getAdditionalInformation()).thenReturn(Collections.singletonMap("id_token", SAMPLE_TOKEN));
    }

    @Test
    public void should_return_authentication_object_if_access_token_can_be_retrived() throws Exception {
        Authentication authentication = oidcAuthFilter.attemptAuthentication(request, response);

        assertThat(authentication).isNotNull();
        verify(oAuth2AccessToken).getAdditionalInformation();
        verify(restTemplate).getAccessToken();
    }

    @Test(expected = AuthenticationServiceException.class)
    public void should_throw_exception_if_cant_get_token() throws Exception {
        when(restTemplate.getAccessToken()).thenThrow(OAuth2Exception.class);

        oidcAuthFilter.attemptAuthentication(request, response);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void should_throw_exception_if_access_token_is_null() throws Exception {
        when(restTemplate.getAccessToken()).thenReturn(null);

        oidcAuthFilter.attemptAuthentication(request, response);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void should_throw_exception_if_no_id_token() throws Exception {
        when(oAuth2AccessToken.getAdditionalInformation()).thenReturn(Collections.emptyMap());

        oidcAuthFilter.attemptAuthentication(request, response);
    }
}
