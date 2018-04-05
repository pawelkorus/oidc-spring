package io.github.pawelkorus.soidc.test;

import io.github.pawelkorus.soidc.DecodeException;
import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.JsonWebKey;
import io.github.pawelkorus.soidc.OIDCTokenDecoder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractOIDCTokenDecoderTest {

    private OIDCTokenDecoder tokenDecoder;

    @Before
    public void setup() {
        tokenDecoder = provideDecoder();
    }

    @Test
    public void rs256_signature() throws Exception {
        decodeAndVerify(rs256Token(), rs256Jwk());
    }

    @Test
    public void es256_signature() throws Exception {
        decodeAndVerify(es256Token(), es256Jwk());
    }

    @Test(expected = DecodeException.class)
    public void should_throw_exception_if_can_cant_find_jwk() throws Exception {
        decodeAndVerify(rs256Token(), null);
    }

    @Test(expected = DecodeException.class)
    public void should_throw_exception_id_cant_parse_token() throws Exception {
        decodeAndVerify("invalid_token", rs256Jwk());
    }

    @Test(expected = DecodeException.class)
    public void should_throw_exception_if_jwk_alg_is_not_known() throws Exception {
        String rs256Token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ0ZXN0Q2xhaW0iOiJjbGFpbVZhbHVlIn0.hnJfI92ZxelOh7zQzuXzyV1ceVWbNQJ4bBW-W37thvg4gLNR1Wf8pimjBtO_9VG8dzi1gYVpy62is8kk9IAu1A";

        JsonWebKey jsonWebKey = new JsonWebKey();
        jsonWebKey.setAlg("SomaRandomNameForAlg23#$$%");

        decodeAndVerify(rs256Token, jsonWebKey);
    }

    @Test(expected = DecodeException.class)
    public void should_throw_exception_if_jwk_alg_doesnt_match_token_alg() throws Exception {
        String rs256Token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ0ZXN0Q2xhaW0iOiJjbGFpbVZhbHVlIn0.hnJfI92ZxelOh7zQzuXzyV1ceVWbNQJ4bBW-W37thvg4gLNR1Wf8pimjBtO_9VG8dzi1gYVpy62is8kk9IAu1A";
        JsonWebKey jsonWebKey = es256Jwk();
        decodeAndVerify(rs256Token, jsonWebKey);
    }

    protected abstract OIDCTokenDecoder provideDecoder();

    private void decodeAndVerify(String token, JsonWebKey jsonWebKey) throws Exception {
        IdTokenPayload idTokenPayload = tokenDecoder.verifyAndDecode(token, (id) -> Optional.ofNullable(jsonWebKey));

        assertThat(idTokenPayload.getAsString("testClaim")).isEqualTo("claimValue");
    }

    private String rs256Token() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ0ZXN0Q2xhaW0iOiJjbGFpbVZhbHVlIn0.hnJfI92ZxelOh7zQzuXzyV1ceVWbNQJ4bBW-W37thvg4gLNR1Wf8pimjBtO_9VG8dzi1gYVpy62is8kk9IAu1A";
    }

    private String es256Token() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJ0ZXN0Q2xhaW0iOiJjbGFpbVZhbHVlIn0.MrvWQao-7KkODozfLEfUnZi9aFbHPFKhiG1PWGmCj7br7MEC2YjxC0JMeuT0szzzkb88Mfay3p5IZHpRqX_Orw";
    }

    private JsonWebKey es256Jwk() {
        JsonWebKey jsonWebKey = new JsonWebKey();
        jsonWebKey.setAlg("ES256");
        jsonWebKey.setParameter("x", Base64.getEncoder().encodeToString(new BigInteger("53781040938123856134006711897117580917785287848602997610956129698715040278907").toByteArray()));
        jsonWebKey.setParameter("y", Base64.getEncoder().encodeToString(new BigInteger("23113885856145266473918225267793482234112866387822336747613735286125197513386").toByteArray()));
        return jsonWebKey;
    }

    private JsonWebKey rs256Jwk() {
        JsonWebKey jsonWebKey = new JsonWebKey();
        jsonWebKey.setAlg("RS256");
        jsonWebKey.setParameter("e", Base64.getEncoder().encodeToString(new BigInteger("65537").toByteArray()));
        jsonWebKey.setParameter("n", Base64.getEncoder().encodeToString(new BigInteger("9453663906642813615651720222389616340520615063962472683444501584853095927678401176645941889756457088027416956831450714234680179120138937334297743287504839").toByteArray()));
        return jsonWebKey;
    }
}
