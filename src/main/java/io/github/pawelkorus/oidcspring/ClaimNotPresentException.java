package io.github.pawelkorus.oidcspring;

public class ClaimNotPresentException extends ClaimAssertException {

    public ClaimNotPresentException(String claim) {
        super("No such claim " + claim);
    }

}
