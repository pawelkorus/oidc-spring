package io.github.pawelkorus.soidc;

import java.time.Instant;
import java.util.List;

public interface IdTokenPayload {

    boolean isNull(String claimId);

    Boolean getAsBoolean(String claimId);

    Integer getAsInt(String claimId);

    Long getAsLong(String claimId);

    Double getAsDouble(String claimId);

    String getAsString(String claimId);

    Instant getAsInstant(String claimId);

    List<String> getAsStringList(String claimId);

}
