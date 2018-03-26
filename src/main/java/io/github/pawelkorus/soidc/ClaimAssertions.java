package io.github.pawelkorus.soidc;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.function.Supplier;

public final class ClaimAssertions {

    public static ClaimAssertion assertRequired(String claimName) throws ClaimNotPresentException {
        return idTokenPayload -> throwIf(idTokenPayload.isNull(claimName), () -> new ClaimNotPresentException(claimName));
    }

    public static ClaimAssertion assertEquals(@NotNull String claimName, @NotNull String expected) throws ClaimValueMismatchException {
        assert claimName != null;
        assert expected != null;
        return idTokenPayload -> {
            String actual = getClaimValueOrDefault(idTokenPayload, claimName, "");
            throwIf(expected.compareTo(actual) != 0, createMessage(claimName, expected, actual));
        };
    }

    public static ClaimAssertion assertDateBeforeNow(String claimName) throws ClaimValueMismatchException {
        return idTokenPayload -> {
            Instant actual = idTokenPayload.getAsInstant(claimName);
            if (actual == null) return;
            Instant now = Instant.now();
            throwIf(now.isBefore(actual), String.format("Incorrect %s claim value. Expected date before noew %s, but got %s", claimName, String.valueOf(now), String.valueOf(actual)));
        };
    }

    public static ClaimAssertion assertDateAfterNow(String claimName) throws ClaimValueMismatchException {
        return idTokenPayload -> {
            Instant actual = idTokenPayload.getAsInstant(claimName);
            if (actual == null) return;
            Instant now = Instant.now();
            throwIf(now.isAfter(actual), String.format("Incorrect claim %s value. Expected date after now %s, but got %s", claimName, String.valueOf(now), String.valueOf(actual)));
        };
    }

    public static ClaimAssertion compound(ClaimAssertion... assertions) throws ClaimValueMismatchException {
        return idTokenPayload -> {
            for (ClaimAssertion claimAssertion : assertions) {
                claimAssertion.check(idTokenPayload);
            }
        };
    }

    public static ClaimAssertion assertIss(@NotNull String issuer) throws ClaimValueMismatchException, ClaimNotPresentException {
        return compound(assertRequired(PublicClaims.iss.name()), assertEquals(PublicClaims.iss.name(), issuer));
    }

    public static ClaimAssertion assertSub() throws ClaimValueMismatchException, ClaimNotPresentException {
        return assertRequired(PublicClaims.sub.name());
    }

    public static ClaimAssertion assertAud(@NotNull String audience) throws ClaimValueMismatchException, ClaimNotPresentException {
        return compound(
            assertRequired(PublicClaims.aud.name()),
            idTokenPayload -> {
                if (idTokenPayload.getAsString(PublicClaims.aud.name()) != null) {
                    throwIf(idTokenPayload.getAsString(PublicClaims.aud.name()).compareTo(audience) != 0, createMessage(PublicClaims.aud.name(), audience, idTokenPayload.getAsString(PublicClaims.aud.name())));
                } else if (idTokenPayload.getAsStringList(PublicClaims.aud.name()) != null) {
                    throwIf(!idTokenPayload.getAsStringList(PublicClaims.aud.name()).contains(audience), createMessage(PublicClaims.aud.name(), audience, idTokenPayload.getAsStringList(PublicClaims.aud.name())));
                } else {
                    throw new ClaimValueMismatchException(String.format("Invalid type of %s claim", PublicClaims.aud.name()));
                }
            });
    }

    public static ClaimAssertion assertExp() throws ClaimValueMismatchException, ClaimNotPresentException {
        return compound(assertRequired(PublicClaims.exp.name()), assertDateAfterNow(PublicClaims.exp.name()));
    }

    public static ClaimAssertion assertNbf() throws ClaimValueMismatchException, ClaimNotPresentException {
        return compound(assertRequired(PublicClaims.nbf.name()), assertDateBeforeNow(PublicClaims.nbf.name()));
    }

    public static ClaimAssertion assertIat() throws ClaimValueMismatchException, ClaimNotPresentException {
        return compound(assertRequired(PublicClaims.iat.name()), assertDateBeforeNow(PublicClaims.iat.name()));
    }

    public static ClaimAssertion assertJti() throws ClaimValueMismatchException, ClaimNotPresentException {
        return assertRequired(PublicClaims.jti.name());
    }

    private static void throwIf(boolean cond, String message) throws ClaimValueMismatchException {
        if (cond) {
            throw new ClaimValueMismatchException(message);
        }
    }

    private static <T extends Exception> void throwIf(boolean cond, Supplier<T> exceptionSupplier) throws T {
        if (cond) {
            throw exceptionSupplier.get();
        }
    }

    private static String createMessage(String claimName, Object expected, Object actual) {
        return String.format("Incorrect %s claim value. Expected %s, but got %s", claimName, String.valueOf(expected), String.valueOf(actual));
    }

    private static String createNullClaimMessage(String claimName) {
        return String.format("Can't check claim value. Claim %s is not set.", claimName);
    }

    private static String getClaimValueOrDefault(@NotNull IdTokenPayload idTokenPayload, @NotNull String claimName, String defaultValue) {
        String value = idTokenPayload.getAsString(claimName);
        if (value == null) value = defaultValue;
        return value;
    }
}
