package io.github.pawelkorus.soidc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClaimAssertionsTest {

    private static String TEST_CLAIM = "test";
    private static String TEST_ISSUER = "testIssuer";
    private static String[] TEST_AUD = new String[]{"testAud1", "testAud2"};

    @Mock
    private IdTokenPayload idTokenPayload;

    @Test(expected = ClaimNotPresentException.class)
    public void assertRequired_should_throw_exception_if_claim_is_not_present() throws Exception {
        when(idTokenPayload.isNull(TEST_CLAIM)).thenReturn(true);
        ClaimAssertions.assertRequired(TEST_CLAIM).check(idTokenPayload);
        verify(idTokenPayload).isNull(TEST_CLAIM);
    }

    @Test
    public void assertRequired_should_pass_if_claim_is_not_null() throws Exception {
        when(idTokenPayload.isNull(TEST_CLAIM)).thenReturn(false);
        ClaimAssertions.assertRequired(TEST_CLAIM).check(idTokenPayload);
        verify(idTokenPayload).isNull(TEST_CLAIM);
    }

    @Test
    public void compound_assertion_should_call_passed_assertions() throws Exception {
        ClaimAssertion assertion1 = mock(ClaimAssertion.class);
        ClaimAssertion assertion2 = mock(ClaimAssertion.class);
        ClaimAssertions.compound(assertion1, assertion2).check(idTokenPayload);
        verify(assertion1).check(idTokenPayload);
        verify(assertion2).check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void compound_assertion_should_fail_if_passed_assertion_fail() throws Exception {
        ClaimAssertion assertion1 = mock(ClaimAssertion.class);
        ClaimAssertion assertion2 = mock(ClaimAssertion.class);
        doThrow(new ClaimValueMismatchException("")).when(assertion2).check(idTokenPayload);
        ClaimAssertions.compound(assertion1, assertion2).check(idTokenPayload);
    }

    @Test
    public void assertDateBefore_should_pass_if_claim_date_is_before_now() throws Exception {
        Instant actual = Instant.now().minus(Duration.ofMinutes(2));
        when(idTokenPayload.getAsInstant(TEST_CLAIM)).thenReturn(actual);
        ClaimAssertions.assertDateBeforeNow(TEST_CLAIM).check(idTokenPayload);
        verify(idTokenPayload).getAsInstant(TEST_CLAIM);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertDateBefore_should_fail_if_claim_date_is_after_now() throws Exception {
        Instant actual = Instant.now().plus(Duration.ofMinutes(2));
        when(idTokenPayload.getAsInstant(TEST_CLAIM)).thenReturn(actual);
        ClaimAssertions.assertDateBeforeNow(TEST_CLAIM).check(idTokenPayload);
    }

    @Test
    public void assertDateAfter_should_passs_if_claim_value_is_after_now() throws Exception {
        Instant actual = Instant.now().plus(Duration.ofMinutes(2));
        when(idTokenPayload.getAsInstant(TEST_CLAIM)).thenReturn(actual);
        ClaimAssertions.assertDateAfterNow(TEST_CLAIM).check(idTokenPayload);
        verify(idTokenPayload).getAsInstant(TEST_CLAIM);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertDateAfter_should_fail_if_claim_value_is_before_now() throws Exception {
        Instant actual = Instant.now().minus(Duration.ofMinutes(2));
        when(idTokenPayload.getAsInstant(TEST_CLAIM)).thenReturn(actual);
        ClaimAssertions.assertDateAfterNow(TEST_CLAIM).check(idTokenPayload);
        verify(idTokenPayload).getAsInstant(TEST_CLAIM);
    }

    @Test
    public void assertIss_should_pass_if_iss_claim_value_match() throws Exception {
        when(idTokenPayload.getAsString(PublicClaims.iss.name())).thenReturn(TEST_ISSUER);
        ClaimAssertions.assertIss(TEST_ISSUER).check(idTokenPayload);
        verify(idTokenPayload).getAsString(PublicClaims.iss.name());
    }

    @Test(expected = ClaimNotPresentException.class)
    public void assertIss_should_fail_if_iss_claim_is_null() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.iss.name())).thenReturn(true);
        ClaimAssertions.assertIss(TEST_ISSUER).check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertIss_should_fail_if_iss_claim_doesnt_match() throws Exception {
        when(idTokenPayload.getAsString(PublicClaims.iss.name())).thenReturn(TEST_ISSUER + "some_additional_data");
        ClaimAssertions.assertIss(TEST_ISSUER).check(idTokenPayload);
    }

    @Test
    public void assertAud_should_pass_if_aud_claim_string_and_match() throws Exception {
        when(idTokenPayload.getAsString(PublicClaims.aud.name())).thenReturn(TEST_AUD[0]);
        ClaimAssertions.assertAud(TEST_AUD[0]).check(idTokenPayload);
        verify(idTokenPayload, atLeast(1)).getAsString(PublicClaims.aud.name());
    }

    @Test
    public void assertAud_should_pass_if_aud_claim_value_array_and_match() throws Exception {
        when(idTokenPayload.getAsStringList(PublicClaims.aud.name())).thenReturn(Arrays.asList(TEST_AUD));
        ClaimAssertions.assertAud(TEST_AUD[0]).check(idTokenPayload);
        verify(idTokenPayload, atLeast(1)).getAsStringList(PublicClaims.aud.name());
    }

    @Test(expected = ClaimNotPresentException.class)
    public void assertAud_should_fail_if_claim_is_null() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.aud.name())).thenReturn(true);
        ClaimAssertions.assertAud(TEST_AUD[0]).check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertAud_should_fail_if_claim_array_doesnt_match() throws Exception {
        when(idTokenPayload.getAsStringList(PublicClaims.aud.name())).thenReturn(Arrays.asList("testAudXXXXX"));
        ClaimAssertions.assertAud(TEST_AUD[0]).check(idTokenPayload);
    }

    @Test
    public void assertSub_should_pass_if_sub_is_present() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.sub.name())).thenReturn(false);
        ClaimAssertions.assertSub().check(idTokenPayload);
        verify(idTokenPayload).isNull(PublicClaims.sub.name());
    }

    @Test(expected = ClaimNotPresentException.class)
    public void assertSub_should_fail_if_sub_is_not_present() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.sub.name())).thenReturn(true);
        ClaimAssertions.assertSub().check(idTokenPayload);
    }

    @Test
    public void assertExp_should_pass_if_claim_date_is_after_now() throws Exception {
        when(idTokenPayload.getAsInstant(PublicClaims.exp.name())).thenReturn(Instant.now().plus(Period.ofDays(1)));
        ClaimAssertions.assertExp().check(idTokenPayload);
        verify(idTokenPayload).getAsInstant(PublicClaims.exp.name());
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertExp_should_fail_if_claim_date_is_before_now() throws Exception {
        when(idTokenPayload.getAsInstant(PublicClaims.exp.name())).thenReturn(Instant.now().minus(Period.ofDays(1)));
        ClaimAssertions.assertExp().check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void assertExp_should_fail_if_claim_is_not_present() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.exp.name())).thenReturn(true);
        ClaimAssertions.assertExp().check(idTokenPayload);
    }

    @Test
    public void assertNbf_should_pass_if_claim_date_is_before_now() throws Exception {
        when(idTokenPayload.getAsInstant(PublicClaims.nbf.name())).thenReturn(Instant.now());
        ClaimAssertions.assertNbf().check(idTokenPayload);
    }

    @Test(expected = ClaimNotPresentException.class)
    public void assertNbf_should_fail_if_claim_is_not_present() throws Exception {
        when(idTokenPayload.isNull(PublicClaims.nbf.name())).thenReturn(true);
        ClaimAssertions.assertNbf().check(idTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void assertNbf_should_fail_if_claim_date_is_after_now() throws Exception {
        when(idTokenPayload.getAsInstant(PublicClaims.nbf.name())).thenReturn(Instant.now().plus(Duration.ofMinutes(1)));
        ClaimAssertions.assertNbf().check(idTokenPayload);
    }
}
