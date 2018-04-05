package io.github.pawelkorus.soidc;

public class DefaultOIDCTokenValidator implements ClaimAssertion {

    private IdentityProviderConfig identityProviderConfig;
    private ClientConfig clientConfig;

    public DefaultOIDCTokenValidator(IdentityProviderConfig identityProviderConfig, ClientConfig clientConfig) {
        this.identityProviderConfig = identityProviderConfig;
        this.clientConfig = clientConfig;
    }

    @Override
    public void check(IdTokenPayload tokenPayload) throws ClaimAssertException {
        ClaimAssertions.assertIss(this.identityProviderConfig.getIssuer()).check(tokenPayload);
        ClaimAssertions.assertAud(this.clientConfig.getClientId()).check(tokenPayload);

        // 4. If the ID Token contains multiple audiences, the Client SHOULD verify that an azp Claim is present.
        // 5. If an azp (authorized party) Claim is present, the Client SHOULD verify that its client_id is the Claim Value.
        if (tokenPayload.getAsStringList(CommonClaim.aud.name()) != null || !tokenPayload.isNull(CommonClaim.azp.name())) {
            ClaimAssertions.assertEquals(CommonClaim.azp.name(), clientConfig.getClientId()).check(tokenPayload);
        }

        ClaimAssertions.assertExp().check(tokenPayload);
        ClaimAssertions.assertIat().check(tokenPayload);

        // 11. If a nonce value was sent in the Authentication Request, a nonce Claim MUST be present and its value checked to verify that it is the same value as the one that was sent in the Authentication Request. The Client SHOULD check the nonce value for replay attacks. The precise method for detecting replay attacks is Client specific.
        // For now nonce claim is not send, so this can't be checked

        // 12. If the acr Claim was requested, the Client SHOULD check that the asserted Claim Value is appropriate. The meaning and processing of acr Claim Values is out of scope for this specification.
        // For now acr claim request is not send, so this can't be checked

        // 13. If the auth_time Claim was requested, either through a specific request for this Claim or by using the max_age parameter, the Client SHOULD check the auth_time Claim value and request re-authentication if it determines too much time has elapsed since the last End-User authentication.
        // For now auth_time claim request is not send, so this can't be checked
    }

}
