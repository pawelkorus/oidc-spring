package io.github.pawelkorus.oidcspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class RemoteIdentityProviderConfigSupplierTest {

    @Test
    public void should_load_Identity_provider_config_from_url() {
        RemoteIdentityProviderConfigSupplier remoteIdentityProviderConfigSupplier =
                RemoteIdentityProviderConfigSupplier.build(getClass().getClassLoader().getResource("0001_sample_discovery_document.json"));

        IdentityProviderConfig identityProviderConfig = remoteIdentityProviderConfigSupplier.get();

        assertThat(identityProviderConfig.getIssuer()).isEqualTo("issuer");
        assertThat(identityProviderConfig.getAuthorizationEndpoint()).isEqualTo("authorization_endpoint");
        assertThat(identityProviderConfig.getTokenEndpoint()).isEqualTo("token_endpoint");
        assertThat(identityProviderConfig.getUserinfoEndpoint()).isEqualTo("userinfo_endpoint");
        assertThat(identityProviderConfig.getRevocationEndpoint()).isEqualTo("revocation_endpoint");
        assertThat(identityProviderConfig.getJwksUri()).isEqualTo("jwks_uri");
        assertThat(identityProviderConfig.getResponseTypesSupported()).containsOnly("code", "token", "id_token", "code token", "code id_token",
                "token id_token", "code token id_token", "none");
        assertThat(identityProviderConfig.getSubjectTypesSupported()).containsOnly("public");
        assertThat(identityProviderConfig.getIdTokenSigningAlgValuesSupported()).containsOnly("RS256");
        assertThat(identityProviderConfig.getScopesSupported()).containsOnly("openid", "email", "profile");
        assertThat(identityProviderConfig.getTokenEndpointAuthMethodsSupported()).containsOnly("client_secret_post", "client_secret_basic");
        assertThat(identityProviderConfig.getClaimsSupported()).containsOnly("aud", "email", "email_verified", "exp", "family_name", "given_name",
                "iat", "iss", "locale", "name", "picture", "sub");
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_runtime_exception_in_case_url_location_is_unaccessible() throws Exception {
        RemoteIdentityProviderConfigSupplier remoteIdentityProviderConfigSupplier = RemoteIdentityProviderConfigSupplier.build(new URL("file:///some_dummy_file023924"));
        remoteIdentityProviderConfigSupplier.get();
    }
}
