package io.github.pawelkorus.soidc.process;

import io.github.pawelkorus.soidc.IdTokenPayload;

public interface TokenProcessor {

    IdTokenPayload verifyAndDecode(String idToken) throws Exception;

}
