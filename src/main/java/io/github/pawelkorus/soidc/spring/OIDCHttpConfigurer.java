package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.*;
import io.github.pawelkorus.soidc.process.TokenPayloadAssertions;
import io.github.pawelkorus.soidc.process.Verifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class OIDCHttpConfigurer extends AbstractHttpConfigurer<OIDCHttpConfigurer, HttpSecurity> {

    private List<IdentityProviderConfigurer> identityProviderConfigurers = new ArrayList<>();

    @Override
    public void init(HttpSecurity builder) throws Exception {
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        ApplicationContext applicationContext = builder.getSharedObject(ApplicationContext.class);
        OAuth2ClientContext oAuth2ClientContext = applicationContext.getBean(OAuth2ClientContext.class);
        OIDCUserDetailsService oidcUserDetailsService = applicationContext.getBean(OIDCUserDetailsService.class);
        OIDCTokenDecoder oidcTokenDecoder = applicationContext.getBean(OIDCTokenDecoder.class);

        for (IdentityProviderConfigurer identityProviderConfigurer : identityProviderConfigurers) {
            ClientConfig clientConfig = identityProviderConfigurer.clientConfig();
            IdentityProviderConfig providerMetadata = identityProviderConfigurer.configuration();

            IdentityProviderResourceDetails identityProviderResourceDetails = configureIdentityProviderResourceDetails(identityProviderConfigurer);


            JsonWebKeyProvider jsonWebKeyProvider = Optional.ofNullable(identityProviderConfigurer.jwkProvider()).orElse(this.createDefaultJwkProvider(providerMetadata));

            Verifier verifier = new Verifier(jsonWebKeyProvider, oidcTokenDecoder);
            TokenPayloadAssertions tokenPayloadAssertions = new TokenPayloadAssertions(verifier);
            tokenPayloadAssertions.assertThat(new DefaultOIDCTokenValidator(providerMetadata, clientConfig));

            OIDCAuthenticationProvider authenticationProvider = new OIDCAuthenticationProvider(providerMetadata, oidcUserDetailsService, tokenPayloadAssertions);

            OIDCAuthFilter openIdConnectFilter = new OIDCAuthFilter(identityProviderConfigurer.requestMatcher(), new OAuth2RestTemplate(identityProviderResourceDetails, oAuth2ClientContext));

            builder
                .authenticationProvider(authenticationProvider)
                .addFilterAfter(openIdConnectFilter, OAuth2ClientContextFilter.class);
        }
    }

    public IdentityProviderConfigurer identityProvider() {
        IdentityProviderConfigurer identityProviderConfigurer = new IdentityProviderConfigurer(AnyRequestMatcher.INSTANCE);
        identityProviderConfigurers.add(identityProviderConfigurer);
        return identityProviderConfigurer;
    }

    public IdentityProviderConfigurer identityProvider(String requestMatcher) {
        IdentityProviderConfigurer identityProviderConfigurer = new IdentityProviderConfigurer(new AntPathRequestMatcher(requestMatcher));
        identityProviderConfigurers.add(identityProviderConfigurer);
        return identityProviderConfigurer;
    }

    public IdentityProviderConfigurer identityProvider(RequestMatcher requestMatcher) {
        IdentityProviderConfigurer identityProviderConfigurer = new IdentityProviderConfigurer(requestMatcher);
        identityProviderConfigurers.add(identityProviderConfigurer);
        return identityProviderConfigurer;
    }

    public static OIDCHttpConfigurer opendIdConnect() {
        return new OIDCHttpConfigurer();
    }

    public final class IdentityProviderConfigurer {

        private String clientId;
        private String clientSecret;
        private String redirectUrl;
        private List<String> requestScopes = new ArrayList<>();
        private IdentityProviderConfig identityProviderConfig;
        private JsonWebKeyProvider jsonWebKeyProvider;
        private RequestMatcher requestMatcher;

        public IdentityProviderConfigurer configuration(Supplier<IdentityProviderConfig> configSupplier) {
            this.identityProviderConfig = configSupplier.get();
            return this;
        }

        public IdentityProviderConfigurer clientCredentials(String clientId, String clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            return this;
        }

        public IdentityProviderConfigurer redirectUrl(String url) {
            this.redirectUrl = url;
            return this;
        }

        public IdentityProviderConfigurer jwkProvider(JsonWebKeyProvider jsonWebKeyProvider) {
            this.jsonWebKeyProvider = jsonWebKeyProvider;
            return this;
        }

        public IdentityProviderConfigurer scopes(String... scopes) {
            requestScopes.addAll(Arrays.asList(scopes));
            return this;
        }

        public OIDCHttpConfigurer and() {
            return OIDCHttpConfigurer.this;
        }

        private IdentityProviderConfigurer(RequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }

        private IdentityProviderConfig configuration() {
            return this.identityProviderConfig;
        }

        private ClientConfig clientConfig() {
            return new ClientConfig(clientId, clientSecret, redirectUrl);
        }

        private JsonWebKeyProvider jwkProvider() {
            return this.jsonWebKeyProvider;
        }

        private RequestMatcher requestMatcher() {
            return this.requestMatcher;
        }

        private List<String> requestScopes() {
            return this.requestScopes;
        }
    }

    private JsonWebKeyProvider createDefaultJwkProvider(IdentityProviderConfig identityProviderConfig) throws Exception {
        String jwksUrl = identityProviderConfig.getJwksUri();
        URL url = new URL(jwksUrl);
        URLConnection connection = url.openConnection();

        return new JsonWebKeySet(connection.getInputStream());
    }

    private IdentityProviderResourceDetails configureIdentityProviderResourceDetails(IdentityProviderConfigurer identityProviderConfigurer) {
        IdentityProviderConfig identityProviderConfig = identityProviderConfigurer.configuration();
        ClientConfig clientConfig = identityProviderConfigurer.clientConfig();

        IdentityProviderResourceDetails identityProviderResourceDetails = new IdentityProviderResourceDetails(identityProviderConfig, clientConfig);
        if (!identityProviderConfigurer.requestScopes().isEmpty()) {
            identityProviderResourceDetails.setScope(identityProviderConfigurer.requestScopes());
        }

        return identityProviderResourceDetails;
    }
}
