package io.github.pawelkorus.oidcspring.process;

import io.github.pawelkorus.oidcspring.IdTokenPayload;
import io.github.pawelkorus.oidcspring.JsonWebKeyProvider;
import io.github.pawelkorus.oidcspring.OIDCTokenDecoder;

import javax.validation.constraints.NotNull;

public class Verifier implements TokenProcessor {

    private final JsonWebKeyProvider jsonWebKeyProvider;
    private final OIDCTokenDecoder decoder;

    public Verifier(@NotNull JsonWebKeyProvider jsonWebKeyProvider,
                    @NotNull OIDCTokenDecoder decoder) {
        this.jsonWebKeyProvider = jsonWebKeyProvider;
        this.decoder = decoder;
    }

    @Override
    public IdTokenPayload verifyAndDecode(String idToken) throws Exception {
        IdTokenPayload tokenPayload = decoder.verifyAndDecode(idToken, jsonWebKeyProvider);
        return tokenPayload;
    }

}
