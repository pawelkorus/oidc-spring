package io.github.pawelkorus.soidc;

import javax.json.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RemoteIdentityProviderConfigSupplier implements Supplier<IdentityProviderConfig> {

    private final URL configUrl;

    private RemoteIdentityProviderConfigSupplier(URL url) {
        this.configUrl = url;
    }

    public static RemoteIdentityProviderConfigSupplier build(@NotNull URL url) {
        return new RemoteIdentityProviderConfigSupplier(url);
    }

    @Override
    public IdentityProviderConfig get() {
        URLConnection connection = null;
        try {
            connection = configUrl.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException("Can't connect to resource to location under following url: " + configUrl.toString());
        }

        try (InputStream inputStream = connection.getInputStream()) {
            JsonReader jsonReader = Json.createReader(inputStream);
            JsonObject jsonObject = jsonReader.readObject();

            IdentityProviderConfig providerMetadata = new IdentityProviderConfig();
            providerMetadata.setIssuer(jsonObject.getString("issuer", null));
            providerMetadata.setAuthorizationEndpoint(jsonObject.getString("authorization_endpoint", null));
            providerMetadata.setTokenEndpoint(jsonObject.getString("token_endpoint", null));
            providerMetadata.setUserinfoEndpoint(jsonObject.getString("userinfo_endpoint", null));
            providerMetadata.setRevocationEndpoint(jsonObject.getString("revocation_endpoint", null));
            providerMetadata.setJwksUri(jsonObject.getString("jwks_uri", null));
            providerMetadata.setResponseTypesSupported(convertToStringList(jsonObject.getJsonArray("response_types_supported")));
            providerMetadata.setSubjectTypesSupported(convertToStringList(jsonObject.getJsonArray("subject_types_supported")));
            providerMetadata.setIdTokenSigningAlgValuesSupported(convertToStringList(jsonObject.getJsonArray("id_token_signing_alg_values_supported")));
            providerMetadata.setScopesSupported(convertToStringList(jsonObject.getJsonArray("scopes_supported")));
            providerMetadata.setTokenEndpointAuthMethodsSupported(convertToStringList(jsonObject.getJsonArray("token_endpoint_auth_methods_supported")));
            providerMetadata.setClaimsSupported(convertToStringList(jsonObject.getJsonArray("claims_supported")));
            return providerMetadata;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<String> convertToStringList(JsonArray jsonArray) {
        if (null == jsonArray) {
            return null;
        }

        return jsonArray.getValuesAs(JsonString.class).stream().map(JsonString::getString).collect(Collectors.toList());
    }
}
