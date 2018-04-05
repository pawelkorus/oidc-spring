package io.github.pawelkorus.soidc.oauth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.pawelkorus.soidc.*;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;

public class OIDCOAuth0TokenDecoder implements OIDCTokenDecoder {

    @Override
    public IdTokenPayload verifyAndDecode(String token, JsonWebKeyProvider jsonWebKeyProvider) throws DecodeException {
        try {
            DecodedJWT jwt = JWT.decode(token);

            String keyId = jwt.getKeyId();
            JsonWebKey jsonWebKey = jsonWebKeyProvider.findOneByKeyId(keyId).orElseThrow(() -> DecodeException.noSuchKey(keyId));

            Algorithm algorithm = AlgorithmFactory.build(jsonWebKey);

            JWTVerifier verification = JWT.require(algorithm).acceptLeeway(Duration.ofMinutes(5).toMillis()).build();

            verification.verify(token);

            return new OAuth0PayloadAdapter(jwt);
        } catch (JWTDecodeException ex) {
            throw new DecodeException("Can't parse ID token", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new DecodeException("Can't verify ID token, due to unknown algorithm", ex);
        } catch (Exception ex) {
            throw new DecodeException("Can't parse and verify ID token.", ex);
        }
    }

}
