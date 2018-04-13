package io.github.pawelkorus.oidcspring.nimbus;

import io.github.pawelkorus.oidcspring.OIDCTokenDecoder;
import io.github.pawelkorus.oidcspring.test.AbstractOIDCTokenDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OIDCNimbusTokenDecoderTest extends AbstractOIDCTokenDecoderTest {

    @Override
    protected OIDCTokenDecoder provideDecoder() {
        return new OIDCNimbusTokenDecoder();
    }

}
