package io.github.pawelkorus.oidcspring.spring;

import io.github.pawelkorus.oidcspring.ClientConfig;
import io.github.pawelkorus.oidcspring.IdentityProviderConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class IdentityProviderResourceDetailsTest {

    private static PodamFactory factory = new PodamFactoryImpl();

    @Test
    public void should_pass_values_to_super_class() {
        IdentityProviderConfig identityProviderConfig = factory.manufacturePojo(IdentityProviderConfig.class);
        ClientConfig clientConfig = factory.manufacturePojo(ClientConfig.class);

        IdentityProviderResourceDetails identityProviderResourceDetails = new IdentityProviderResourceDetails(identityProviderConfig, clientConfig);

        assertThat(identityProviderResourceDetails.getClientId()).isEqualTo(clientConfig.getClientId());
        assertThat(identityProviderResourceDetails.getClientSecret()).isEqualTo(clientConfig.getClientSecret());
        assertThat(identityProviderResourceDetails.getPreEstablishedRedirectUri()).isEqualTo(clientConfig.getRedirectUri());
        assertThat(identityProviderResourceDetails.getId()).isEqualTo(identityProviderConfig.getIssuer());
        assertThat(identityProviderResourceDetails.getAccessTokenUri()).isEqualTo(identityProviderConfig.getTokenEndpoint());
        assertThat(identityProviderResourceDetails.getUserAuthorizationUri()).isEqualTo(identityProviderConfig.getAuthorizationEndpoint());
        assertThat(identityProviderResourceDetails.getScope()).containsOnly("openid");
        assertThat(identityProviderResourceDetails.isUseCurrentUri()).isFalse();
    }

}
