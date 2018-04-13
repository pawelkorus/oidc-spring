package io.github.pawelkorus.oidcspring;

@FunctionalInterface
public interface ClaimAssertion {

    void check(IdTokenPayload tokenPayload) throws ClaimAssertException;

}
