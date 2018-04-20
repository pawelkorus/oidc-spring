package io.github.pawelkorus.oidcspring.oauth0;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import io.github.pawelkorus.oidcspring.IdTokenPayload;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OAuth0PayloadAdapter implements IdTokenPayload {

    private final Payload payload;

    OAuth0PayloadAdapter(Payload payload) {
        this.payload = payload;
    }

    private Optional<Claim> getClaim(String id) {
        return Optional.ofNullable(payload.getClaim(id));
    }

    @Override
    public boolean isNull(String claimId) {
        return getClaim(claimId).map(Claim::isNull).orElse(true);
    }

    @Override
    public Boolean getAsBoolean(String claimId) {
        return getClaim(claimId).map(Claim::asBoolean).orElse(null);
    }

    @Override
    public Integer getAsInt(String claimId) {
        return getClaim(claimId).map(Claim::asInt).orElse(null);
    }

    @Override
    public Long getAsLong(String claimId) {
        return getClaim(claimId).map(Claim::asLong).orElse(null);
    }

    @Override
    public Double getAsDouble(String claimId) {
        return getClaim(claimId).map(Claim::asDouble).orElse(null);
    }

    @Override
    public String getAsString(String claimId) {
        return getClaim(claimId).map(Claim::asString).orElse(null);
    }

    @Override
    public Instant getAsInstant(String claimId) {
        return getClaim(claimId).map(Claim::asDate).map(Date::toInstant).orElse(null);
    }

    @Override
    public List<String> getAsStringList(String claimId) {
        return getClaim(claimId).map(c -> c.asList(String.class)).orElse(null);
    }
}
