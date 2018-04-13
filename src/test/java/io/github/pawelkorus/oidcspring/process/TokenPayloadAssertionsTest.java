package io.github.pawelkorus.oidcspring.process;

import io.github.pawelkorus.oidcspring.ClaimAssertion;
import io.github.pawelkorus.oidcspring.ClaimValueMismatchException;
import io.github.pawelkorus.oidcspring.IdTokenPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenPayloadAssertionsTest {

    private static String SAMPLE_IDTOKEN = "sampleidtoken";

    @Mock
    IdTokenPayload mockedIdTokenPayload;

    @Mock
    TokenProcessor mockedTokenProcessor;

    @InjectMocks
    TokenPayloadAssertions tokenPayloadAssertions;

    @Before
    public void setup() throws Exception {
        when(mockedTokenProcessor.verifyAndDecode(SAMPLE_IDTOKEN)).thenReturn(mockedIdTokenPayload);
    }

    @Test
    public void should_check_all_assertions() throws Exception {
        List<ClaimAssertion> claimAssertions = Arrays.asList(mock(ClaimAssertion.class), mock(ClaimAssertion.class));
        claimAssertions.forEach(tokenPayloadAssertions::assertThat);

        tokenPayloadAssertions.verifyAndDecode(SAMPLE_IDTOKEN);

        verify(mockedTokenProcessor).verifyAndDecode(SAMPLE_IDTOKEN);
        verify(claimAssertions.get(0)).check(mockedIdTokenPayload);
        verify(claimAssertions.get(1)).check(mockedIdTokenPayload);
    }

    @Test(expected = ClaimValueMismatchException.class)
    public void should_throw_exception_if_assertion_throws_exception() throws Exception {
        ClaimAssertion claimAssertion = mock(ClaimAssertion.class);
        doThrow(new ClaimValueMismatchException("")).when(claimAssertion).check(mockedIdTokenPayload);
        tokenPayloadAssertions.assertThat(claimAssertion);

        tokenPayloadAssertions.verifyAndDecode(SAMPLE_IDTOKEN);
    }
}
