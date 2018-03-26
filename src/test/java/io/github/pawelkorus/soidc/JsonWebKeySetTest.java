package io.github.pawelkorus.soidc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
public class JsonWebKeySetTest {

    @Test
    public void shouldLoadJWKSJson() throws Exception {
        try (
            FileInputStream sampleJWKS = new FileInputStream(new File("src/test/resources/0001_sample_jwks.json"));
        ) {
            JsonWebKeySet jwks = new JsonWebKeySet(sampleJWKS);

            Optional<JsonWebKey> optjwk = jwks.findOneByKeyId("kid");
            assertThat(optjwk).isNotEmpty();
            optjwk.ifPresent(jwk -> {
                assertThat(jwk.getKeyId()).isEqualTo("kid");
                assertThat(jwk.getKeyType()).isEqualTo("RSA");
                assertThat(jwk.getAlg()).isEqualTo("RS256");
                assertThat(jwk.getUse()).isEqualTo("sig");
                assertThat(jwk.getX5C()).contains("sample_x5c", "sample2_x5c");
                assertThat(jwk.getX5T()).isEqualTo("sample_x5t");
            });

            Optional<JsonWebKey> opt2jwk = jwks.findOneByKeyId("kidx");
            assertThat(opt2jwk).isNotEmpty();
            opt2jwk.ifPresent(jwk -> {
                assertThat(jwk.getKeyId()).isEqualTo("kidx");
                assertThat(jwk.getKeyType()).isEqualTo("RSA");
                assertThat(jwk.getAlg()).isEqualTo("RS256");
                assertThat(jwk.getUse()).isEqualTo("enc");
                assertThat(jwk.getX5C()).contains("samplex_x5c", "samplex2_x5c");
                assertThat(jwk.getX5T()).isEqualTo("samplex_x5t");
            });
        }
    }
}
