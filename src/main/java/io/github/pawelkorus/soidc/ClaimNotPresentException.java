package io.github.pawelkorus.soidc;

public class ClaimNotPresentException extends ClaimAssertException {

    public ClaimNotPresentException(String claim) {
        super("No such claim " + claim);
    }

}
