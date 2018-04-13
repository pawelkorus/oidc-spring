package io.github.pawelkorus.oidcspring.process;

import io.github.pawelkorus.oidcspring.ClaimAssertion;
import io.github.pawelkorus.oidcspring.IdTokenPayload;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class TokenPayloadAssertions implements TokenProcessor {

    private List<ClaimAssertion> assertions = new ArrayList<>();
    private TokenProcessor tokenProcessor;

    public TokenPayloadAssertions(@NotNull TokenProcessor tokenProcessor) {
        this.tokenProcessor = tokenProcessor;
    }

    public TokenPayloadAssertions assertThat(ClaimAssertion assertion) {
        this.assertions.add(assertion);
        return this;
    }

    @Override
    public IdTokenPayload verifyAndDecode(String idToken) throws Exception {
        IdTokenPayload idTokenPayload = tokenProcessor.verifyAndDecode(idToken);

        for (ClaimAssertion claimAssertion : assertions) {
            claimAssertion.check(idTokenPayload);
        }

        return idTokenPayload;
    }
}
