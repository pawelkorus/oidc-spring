package io.github.pawelkorus.oidcspring.process;

import io.github.pawelkorus.oidcspring.IdTokenPayload;

public interface TokenProcessor {

    IdTokenPayload verifyAndDecode(String idToken) throws Exception;

}
