package io.github.pawelkorus.oidcspring.nimbus;

import com.nimbusds.jwt.JWTClaimsSet;
import io.github.pawelkorus.oidcspring.test.AbstractIdTokenPayloadTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class NimbusPayloadAdapterTest extends AbstractIdTokenPayloadTest {

    private JWTClaimsSet jwtClaimsSet;
    private NimbusPayloadAdapter nimbusPayloadAdapter;

    @Before
    public void setup() {
        jwtClaimsSet = new JWTClaimsSet.Builder().build();
        nimbusPayloadAdapter = new NimbusPayloadAdapter(jwtClaimsSet);
    }

    @Test
    public void should_return_null_if_claim_is_not_present() {
        assertThat(getters(nimbusPayloadAdapter).stream().map(fun -> fun.apply(RANDOM_CLAIM_NAME))).containsOnlyNulls();
    }

    @Test
    public void getter_should_return_correct_value() {
        Date now = new Date();

        jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim("boolean", Boolean.TRUE)
                .claim("double", 10d)
                .claim("date", now)
                .claim("int", 10)
                .claim("long", 10L)
                .claim("string", "sampleString")
                .claim("stringList", Arrays.asList("sample1", "sample2"))
                .build();
        nimbusPayloadAdapter = new NimbusPayloadAdapter(jwtClaimsSet);

        assertThat(nimbusPayloadAdapter.getAsBoolean("boolean")).isTrue();
        assertThat(nimbusPayloadAdapter.getAsDouble("double")).isEqualTo(10d);
        assertThat(nimbusPayloadAdapter.getAsInstant("date")).isEqualTo(now.toInstant());
        assertThat(nimbusPayloadAdapter.getAsInt("int")).isEqualTo(10);
        assertThat(nimbusPayloadAdapter.getAsLong("long")).isEqualTo(10L);
        assertThat(nimbusPayloadAdapter.getAsString("string")).isEqualTo("sampleString");
        assertThat(nimbusPayloadAdapter.getAsStringList("stringList")).containsOnly("sample1", "sample2");
    }

    @Test
    public void getter_should_return_null_if_object_has_incorrect_type() {
        Object sampleObject = new Object();

        jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim("boolean", sampleObject)
                .claim("double", sampleObject)
                .claim("date", sampleObject)
                .claim("int", sampleObject)
                .claim("long", sampleObject)
                .claim("string", sampleObject)
                .claim("stringList", sampleObject)
                .build();
        nimbusPayloadAdapter = new NimbusPayloadAdapter(jwtClaimsSet);

        assertThat(nimbusPayloadAdapter.getAsBoolean("boolean")).isNull();
        assertThat(nimbusPayloadAdapter.getAsDouble("double")).isNull();
        assertThat(nimbusPayloadAdapter.getAsInt("int")).isNull();
        assertThat(nimbusPayloadAdapter.getAsLong("long")).isNull();
        assertThat(nimbusPayloadAdapter.getAsString("string")).isNull();
        assertThat(nimbusPayloadAdapter.getAsStringList("stringList")).isNull();
    }

    @Test
    public void is_null_should_return_true_if_claim_not_present() {
        assertThat(nimbusPayloadAdapter.isNull(RANDOM_CLAIM_NAME)).isTrue();
    }

    @Test
    public void is_null_should_return_false_if_claim_is_not_null() {
        jwtClaimsSet = new JWTClaimsSet.Builder().claim(RANDOM_CLAIM_NAME, "someValue").build();
        nimbusPayloadAdapter = new NimbusPayloadAdapter(jwtClaimsSet);
        assertThat(nimbusPayloadAdapter.isNull(RANDOM_CLAIM_NAME)).isFalse();
    }
}
