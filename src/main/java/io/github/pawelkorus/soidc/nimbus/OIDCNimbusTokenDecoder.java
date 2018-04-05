package io.github.pawelkorus.soidc.nimbus;

import com.nimbusds.jwt.SignedJWT;
import io.github.pawelkorus.soidc.*;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class OIDCNimbusTokenDecoder implements OIDCTokenDecoder {

    @Override
    public IdTokenPayload verifyAndDecode(String idToken, JsonWebKeyProvider jsonWebKeyProvider) throws DecodeException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);

            String keyId = signedJWT.getHeader().getKeyID();
            JsonWebKey jsonWebKey = jsonWebKeyProvider.findOneByKeyId(keyId).orElseThrow(() -> DecodeException.noSuchKey(keyId));

            signedJWT.verify(VerifierFactory.build(jsonWebKey));

            return new NimbusPayloadAdapter(signedJWT.getJWTClaimsSet());
        } catch (ParseException ex) {
            throw new DecodeException("Can't parse ID token", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new DecodeException("Can't verify ID token, due to unknown algorithm", ex);
        } catch (Exception ex) {
            throw new DecodeException("Can't parse and verify ID token.", ex);
        }
    }

}
