package io.github.pawelkorus.soidc.nimbus;

import com.nimbusds.jwt.SignedJWT;
import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.JsonWebKey;
import io.github.pawelkorus.soidc.JsonWebKeyProvider;
import io.github.pawelkorus.soidc.OIDCTokenDecoder;

public class OIDCNimbusTokenDecoder implements OIDCTokenDecoder {

    @Override
    public IdTokenPayload verifyAndDecode(String idToken, JsonWebKeyProvider jsonWebKeyProvider) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(idToken);

        String keyId = signedJWT.getHeader().getKeyID();
        JsonWebKey jsonWebKey = jsonWebKeyProvider.findOneByKeyId(keyId).orElseThrow(() -> new Exception("Can't find key with id " + keyId));

        signedJWT.verify(VerifierFactory.build(jsonWebKey));

        return new NimbusPayloadAdapter(signedJWT.getJWTClaimsSet());
    }

}
