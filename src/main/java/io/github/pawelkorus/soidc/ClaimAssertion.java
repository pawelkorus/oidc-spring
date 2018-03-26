package io.github.pawelkorus.soidc;

@FunctionalInterface
public interface ClaimAssertion {

    void check(IdTokenPayload tokenPayload) throws ClaimAssertException;

}
