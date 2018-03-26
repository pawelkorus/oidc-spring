package io.github.pawelkorus.soidc.process;

import io.github.pawelkorus.soidc.JsonWebKeyProvider;
import io.github.pawelkorus.soidc.OIDCTokenDecoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VerifierTest {

    private String TEST_TOKEN = "testToken";

    @Mock
    private OIDCTokenDecoder oidcTokenDecoder;

    @Mock
    private JsonWebKeyProvider jsonWebKeyProvider;

    @InjectMocks
    private Verifier verifier;

    @Test
    public void should_try_to_decode_token_with_given_decoder() throws Exception {
        verifier.verifyAndDecode(TEST_TOKEN);

        verify(oidcTokenDecoder).verifyAndDecode(TEST_TOKEN, jsonWebKeyProvider);
    }
}
