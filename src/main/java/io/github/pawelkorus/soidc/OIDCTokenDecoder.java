package io.github.pawelkorus.soidc;

public interface OIDCTokenDecoder {

    IdTokenPayload verifyAndDecode(String idToken, JsonWebKeyProvider jsonWebKeyProvider) throws Exception;

}
