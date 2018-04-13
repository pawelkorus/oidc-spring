package io.github.pawelkorus.oidcspring;

import java.util.Optional;

public interface JsonWebKeyProvider {

    Optional<JsonWebKey> findOneByKeyId(String kid);

}
