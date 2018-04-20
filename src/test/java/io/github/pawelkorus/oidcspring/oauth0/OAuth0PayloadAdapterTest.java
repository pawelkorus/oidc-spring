package io.github.pawelkorus.oidcspring.oauth0;

import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import io.github.pawelkorus.oidcspring.test.AbstractIdTokenPayloadTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OAuth0PayloadAdapterTest extends AbstractIdTokenPayloadTest {

    @Mock
    private Payload payload;

    @InjectMocks
    private OAuth0PayloadAdapter oAuth0PayloadAdapter;

    @Test
    public void should_return_null_if_claim_is_not_present() {
        assertThat(getters(oAuth0PayloadAdapter).stream().map(fun -> fun.apply(RANDOM_CLAIM_NAME))).containsOnlyNulls();
    }

    @Test
    public void getters_should_call_payload() {
        Collection<Function<String, Object>> getters = getters(oAuth0PayloadAdapter);

        getters.forEach(fun -> fun.apply(RANDOM_CLAIM_NAME));

        verify(payload, times(getters.size())).getClaim(RANDOM_CLAIM_NAME);
    }

    @Test
    public void is_null_should_return_true_if_claim_not_present() {
        assertThat(oAuth0PayloadAdapter.isNull(RANDOM_CLAIM_NAME)).isTrue();
    }

    @Test
    public void is_null_should_return_true_if_claim_is_null() {
        when(payload.getClaim(RANDOM_CLAIM_NAME)).thenReturn(new NullClaim());

        assertThat(oAuth0PayloadAdapter.isNull(RANDOM_CLAIM_NAME)).isTrue();
        verify(payload).getClaim(RANDOM_CLAIM_NAME);
    }

    @Test
    public void is_null_should_return_false_if_claim_is_not_null() {
        when(payload.getClaim(RANDOM_CLAIM_NAME)).thenReturn(mock(Claim.class));

        assertThat(oAuth0PayloadAdapter.isNull(RANDOM_CLAIM_NAME)).isFalse();
        verify(payload).getClaim(RANDOM_CLAIM_NAME);
    }

}
