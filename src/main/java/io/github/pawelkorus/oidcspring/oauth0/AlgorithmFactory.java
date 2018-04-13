package io.github.pawelkorus.oidcspring.oauth0;

import com.auth0.jwt.algorithms.Algorithm;
import io.github.pawelkorus.oidcspring.JsonWebKey;
import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

public class AlgorithmFactory {

    public static Algorithm build(JsonWebKey jwk) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if ("RS256".compareTo(jwk.getAlg()) == 0) {
            BigInteger modulus = new BigInteger(1, Base64.decodeBase64(jwk.getStringParameter("n")));
            BigInteger exponent = new BigInteger(1, Base64.decodeBase64(jwk.getStringParameter("e")));

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);

            return Algorithm.RSA256(pub, null);
        } else if ("ES256".compareTo(jwk.getAlg()) == 0) {
            BigInteger x = new BigInteger(1, Base64.decodeBase64(jwk.getStringParameter("x")));
            BigInteger y = new BigInteger(1, Base64.decodeBase64(jwk.getStringParameter("y")));
            ECPoint ecPoint = new ECPoint(x, y);

            ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(ecPoint, AlgorithmFactory.P256);

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            ECPublicKey ecPublicKey = (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);

            return Algorithm.ECDSA256(ecPublicKey, null);
        }

        throw new NoSuchAlgorithmException(String.format("Can't recognise %s algorithm", jwk.getAlg()));
    }

    public static final ECParameterSpec P256 = new ECParameterSpec(
        new EllipticCurve(
            // field the finite field that this elliptic curve is over.
            new ECFieldFp(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
            // a the first coefficient of this elliptic curve.
            new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
            // b the second coefficient of this elliptic curve.
            new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291")
        ),
        //g the generator which is also known as the base point.
        new ECPoint(
            // gx
            new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
            // gy
            new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109")
        ),
        // Order n
        new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
        1);

}
