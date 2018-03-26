package io.github.pawelkorus.soidc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOIDCTokenValidatorTest {

    private static String TEST_CLIENT_ID = "TEST_CLIENT_ID";
    private static String TEST_ISS = "TEST_ISS";

    @Mock
    ClientConfig clientConfig;

    @Mock
    IdentityProviderConfig identityProviderConfig;

    @Mock
    IdTokenPayload idTokenPayload;

    @InjectMocks
    DefaultOIDCTokenValidator defaultOIDCTokenValidator;

    @Before
    public void setup() {
        when(clientConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(identityProviderConfig.getIssuer()).thenReturn(TEST_ISS);

        whenClaimValue(PublicClaims.iss, TEST_ISS);
        whenClaimValue(PublicClaims.aud, TEST_CLIENT_ID);
        whenClaimValue(PublicClaims.exp, Instant.now().plus(Duration.ofDays(2)));
        whenClaimValue(PublicClaims.iat, Instant.now().minus(Duration.ofDays(2)));
        whenClaimValue(PublicClaims.azp, TEST_CLIENT_ID);
    }

    @Test
    public void should_pass_if_everything_is_ok() throws Exception {
        defaultOIDCTokenValidator.check(idTokenPayload);
        verify(idTokenPayload, atLeast(1)).getAsString(PublicClaims.iss.name());
        verify(idTokenPayload, atLeast(1)).getAsString(PublicClaims.aud.name());
        verify(idTokenPayload, atLeast(1)).getAsString(PublicClaims.azp.name());
        verify(idTokenPayload, atLeast(1)).getAsInstant(PublicClaims.exp.name());
        verify(idTokenPayload, atLeast(1)).getAsInstant(PublicClaims.iat.name());
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_iss_is_not_present() throws Exception {
        whenClaimValue(PublicClaims.iss, (String) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_iss_not_match() throws Exception {
        whenClaimValue(PublicClaims.iss, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_aud_is_not_present() throws Exception {
        whenClaimValue(PublicClaims.aud, (String) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_doesnt_match() throws Exception {
        whenClaimValue(PublicClaims.aud, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_is_array_and_doesnt_contain_aud() throws Exception {
        whenClaimValue(PublicClaims.aud, (String) null);
        whenClaimValue(PublicClaims.aud, Arrays.asList("xxx", "yyy"));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_is_array_and_azp_is_doesnt_match() throws Exception {
        whenClaimValue(PublicClaims.aud, (String) null);
        whenClaimValue(PublicClaims.aud, Arrays.asList("xxx", TEST_CLIENT_ID));
        whenClaimValue(PublicClaims.azp, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_exp_claim_is_not_present() throws Exception {
        whenClaimValue(PublicClaims.exp, (Instant) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_exp_is_incorrect() throws Exception {
        whenClaimValue(PublicClaims.exp, Instant.now().minus(Duration.ofDays(2)));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_iat_claim_is_not_present() throws Exception {
        whenClaimValue(PublicClaims.iat, (Instant) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_iat_is_incorrect() throws Exception {
        whenClaimValue(PublicClaims.iat, Instant.now().plus(Duration.ofDays(2)));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    private void whenClaimValue(PublicClaims publicClaims, String value) {
        when(idTokenPayload.getAsString(publicClaims.name())).thenReturn(value);
        when(idTokenPayload.isNull(publicClaims.name())).thenReturn(value == null);
    }

    private void whenClaimValue(PublicClaims publicClaims, Instant value) {
        when(idTokenPayload.getAsInstant(publicClaims.name())).thenReturn(value);
        when(idTokenPayload.isNull(publicClaims.name())).thenReturn(value == null);
    }

    private void whenClaimValue(PublicClaims publicClaims, List<String> value) {
        when(idTokenPayload.getAsStringList(publicClaims.name())).thenReturn(value);
        when(idTokenPayload.isNull(publicClaims.name())).thenReturn(value == null);
    }
}
