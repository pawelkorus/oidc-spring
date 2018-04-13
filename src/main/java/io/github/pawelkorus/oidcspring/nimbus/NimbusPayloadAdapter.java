package io.github.pawelkorus.oidcspring.nimbus;

import com.nimbusds.jwt.JWTClaimsSet;
import io.github.pawelkorus.oidcspring.IdTokenPayload;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class NimbusPayloadAdapter implements IdTokenPayload {

    private final JWTClaimsSet claimsSet;

    NimbusPayloadAdapter(JWTClaimsSet claimsSet) {
        this.claimsSet = claimsSet;
    }

    @Override
    public boolean isNull(String claimId) {
        return claimsSet.getClaim(claimId) == null;
    }

    @Override
    public Boolean getAsBoolean(String claimId) {
        return nullIfException(claimsSet::getBooleanClaim, claimId);
    }

    @Override
    public Integer getAsInt(String claimId) {
        return nullIfException(claimsSet::getIntegerClaim, claimId);
    }

    @Override
    public Long getAsLong(String claimId) {
        return nullIfException(claimsSet::getLongClaim, claimId);
    }

    @Override
    public Double getAsDouble(String claimId) {
        return nullIfException(claimsSet::getDoubleClaim, claimId);
    }

    @Override
    public String getAsString(String claimId) {
        return nullIfException(claimsSet::getStringClaim, claimId);
    }

    @Override
    public Instant getAsInstant(String claimId) {
        Date result = nullIfException(claimsSet::getDateClaim, claimId);
        return result == null ? null : result.toInstant();
    }

    @Override
    public List<String> getAsStringList(String claimId) {
        return nullIfException(claimsSet::getStringListClaim, claimId);
    }

    private <T, R> R nullIfException(ParseFunction<T, R> fun, T arg) {
        try {
            return fun.apply(arg);
        } catch (ParseException ex) {
            return null;
        }
    }

    @FunctionalInterface
    private static interface ParseFunction<T, R> {
        R apply(T var1) throws ParseException;
    }
}
