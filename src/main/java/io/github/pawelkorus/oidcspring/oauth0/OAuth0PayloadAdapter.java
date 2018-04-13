package io.github.pawelkorus.oidcspring.oauth0;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import io.github.pawelkorus.oidcspring.IdTokenPayload;

import java.time.Instant;
import java.util.List;

public class OAuth0PayloadAdapter implements IdTokenPayload {

    private final Payload payload;

    OAuth0PayloadAdapter(Payload payload) {
        this.payload = payload;
    }

    private Claim getClaim(String id) {
        return payload.getClaim(id);
    }

    @Override
    public boolean isNull(String claimId) {
        return getClaim(claimId).isNull();
    }

    @Override
    public Boolean getAsBoolean(String claimId) {
        return getClaim(claimId).asBoolean();
    }

    @Override
    public Integer getAsInt(String claimId) {
        return getClaim(claimId).asInt();
    }

    @Override
    public Long getAsLong(String claimId) {
        return getClaim(claimId).asLong();
    }

    @Override
    public Double getAsDouble(String claimId) {
        return getClaim(claimId).asDouble();
    }

    @Override
    public String getAsString(String claimId) {
        return getClaim(claimId).asString();
    }

    @Override
    public Instant getAsInstant(String claimId) {
        return getClaim(claimId).asDate().toInstant();
    }

    @Override
    public List<String> getAsStringList(String claimId) {
        return getClaim(claimId).asList(String.class);
    }
}
