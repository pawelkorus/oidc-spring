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

        whenClaimValue(CommonClaim.iss, TEST_ISS);
        whenClaimValue(CommonClaim.aud, TEST_CLIENT_ID);
        whenClaimValue(CommonClaim.exp, Instant.now().plus(Duration.ofDays(2)));
        whenClaimValue(CommonClaim.iat, Instant.now().minus(Duration.ofDays(2)));
        whenClaimValue(CommonClaim.azp, TEST_CLIENT_ID);
    }

    @Test
    public void should_pass_if_everything_is_ok() throws Exception {
        defaultOIDCTokenValidator.check(idTokenPayload);
        verify(idTokenPayload, atLeast(1)).getAsString(CommonClaim.iss.name());
        verify(idTokenPayload, atLeast(1)).getAsString(CommonClaim.aud.name());
        verify(idTokenPayload, atLeast(1)).getAsString(CommonClaim.azp.name());
        verify(idTokenPayload, atLeast(1)).getAsInstant(CommonClaim.exp.name());
        verify(idTokenPayload, atLeast(1)).getAsInstant(CommonClaim.iat.name());
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_iss_is_not_present() throws Exception {
        whenClaimValue(CommonClaim.iss, (String) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_iss_not_match() throws Exception {
        whenClaimValue(CommonClaim.iss, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_aud_is_not_present() throws Exception {
        whenClaimValue(CommonClaim.aud, (String) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_doesnt_match() throws Exception {
        whenClaimValue(CommonClaim.aud, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_is_array_and_doesnt_contain_aud() throws Exception {
        whenClaimValue(CommonClaim.aud, (String) null);
        whenClaimValue(CommonClaim.aud, Arrays.asList("xxx", "yyy"));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_aud_is_array_and_azp_is_doesnt_match() throws Exception {
        whenClaimValue(CommonClaim.aud, (String) null);
        whenClaimValue(CommonClaim.aud, Arrays.asList("xxx", TEST_CLIENT_ID));
        whenClaimValue(CommonClaim.azp, "xxx");
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_exp_claim_is_not_present() throws Exception {
        whenClaimValue(CommonClaim.exp, (Instant) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_exp_is_incorrect() throws Exception {
        whenClaimValue(CommonClaim.exp, Instant.now().minus(Duration.ofDays(2)));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void should_fail_if_iat_claim_is_not_present() throws Exception {
        whenClaimValue(CommonClaim.iat, (Instant) null);
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_fail_if_iat_is_incorrect() throws Exception {
        whenClaimValue(CommonClaim.iat, Instant.now().plus(Duration.ofDays(2)));
        defaultOIDCTokenValidator.check(idTokenPayload);
    }

    private void whenClaimValue(CommonClaim commonClaim, String value) {
        when(idTokenPayload.getAsString(commonClaim.name())).thenReturn(value);
        when(idTokenPayload.isNull(commonClaim.name())).thenReturn(value == null);
    }

    private void whenClaimValue(CommonClaim commonClaim, Instant value) {
        when(idTokenPayload.getAsInstant(commonClaim.name())).thenReturn(value);
        when(idTokenPayload.isNull(commonClaim.name())).thenReturn(value == null);
    }

    private void whenClaimValue(CommonClaim commonClaim, List<String> value) {
        when(idTokenPayload.getAsStringList(commonClaim.name())).thenReturn(value);
        when(idTokenPayload.isNull(commonClaim.name())).thenReturn(value == null);
    }
}
