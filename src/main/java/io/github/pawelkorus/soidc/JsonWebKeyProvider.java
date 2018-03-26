package io.github.pawelkorus.soidc;

import java.util.Optional;

public interface JsonWebKeyProvider {

    Optional<JsonWebKey> findOneByKeyId(String kid);

}
