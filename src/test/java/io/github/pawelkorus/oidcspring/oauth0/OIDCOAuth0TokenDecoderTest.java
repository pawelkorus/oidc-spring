package io.github.pawelkorus.oidcspring.oauth0;

import io.github.pawelkorus.oidcspring.OIDCTokenDecoder;
import io.github.pawelkorus.oidcspring.test.AbstractOIDCTokenDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OIDCOAuth0TokenDecoderTest extends AbstractOIDCTokenDecoderTest {

    @Override
    protected OIDCTokenDecoder provideDecoder() {
        return new OIDCOAuth0TokenDecoder();
    }

}
