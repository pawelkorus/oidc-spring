package io.github.pawelkorus.soidc;

public class DecodeException extends Exception {

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DecodeException noSuchKey(String kid) {
        return new DecodeException("Can't find JWK with id " + kid);
    }
}
