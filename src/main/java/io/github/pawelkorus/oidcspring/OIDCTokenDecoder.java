package io.github.pawelkorus.oidcspring;

public interface OIDCTokenDecoder {

    IdTokenPayload verifyAndDecode(String idToken, JsonWebKeyProvider jsonWebKeyProvider) throws DecodeException;

}
