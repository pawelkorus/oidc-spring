package io.github.pawelkorus.oidcspring.test;

import io.github.pawelkorus.oidcspring.IdTokenPayload;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractIdTokenPayloadTest {

    protected static String RANDOM_CLAIM_NAME = UUID.randomUUID().toString();

    protected Collection<Function<String, Object>> getters(IdTokenPayload idTokenPayload) {
        return Arrays.asList(idTokenPayload::getAsBoolean,
                idTokenPayload::getAsDouble,
                idTokenPayload::getAsInstant,
                idTokenPayload::getAsInt,
                idTokenPayload::getAsLong,
                idTokenPayload::getAsString,
                idTokenPayload::getAsStringList);
    }
}
