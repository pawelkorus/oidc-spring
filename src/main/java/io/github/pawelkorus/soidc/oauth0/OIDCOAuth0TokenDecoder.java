package io.github.pawelkorus.soidc.oauth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.JsonWebKey;
import io.github.pawelkorus.soidc.JsonWebKeyProvider;
import io.github.pawelkorus.soidc.OIDCTokenDecoder;

public class OIDCOAuth0TokenDecoder implements OIDCTokenDecoder {

    @Override
    public IdTokenPayload verifyAndDecode(String token, JsonWebKeyProvider jsonWebKeyProvider) throws Exception {
        DecodedJWT jwt = JWT.decode(token);

        String keyId = jwt.getKeyId();
        JsonWebKey jsonWebKey = jsonWebKeyProvider.findOneByKeyId(keyId).orElseThrow(() -> new Exception("Can't find key with id " + keyId));

        Algorithm algorithm = AlgorithmFactory.build(jsonWebKey);

        JWTVerifier verification = JWT.require(algorithm).build();

        verification.verify(token);

        return new OAuth0PayloadAdapter(jwt);
    }

}
