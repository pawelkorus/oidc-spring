package io.github.pawelkorus.soidc.oauth0;

import io.github.pawelkorus.soidc.OIDCTokenDecoder;
import io.github.pawelkorus.soidc.test.AbstractOIDCTokenDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OIDCOAuth0TokenDecoderTest extends AbstractOIDCTokenDecoderTest {

    @Override
    protected OIDCTokenDecoder provideDecoder() {
        return new OIDCOAuth0TokenDecoder();
    }

}
