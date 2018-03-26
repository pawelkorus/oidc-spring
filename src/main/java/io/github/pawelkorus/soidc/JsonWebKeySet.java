package io.github.pawelkorus.soidc;

import javax.json.*;
import java.io.InputStream;
import java.util.*;

public class JsonWebKeySet implements JsonWebKeyProvider {

    private Set<JsonWebKey> keys = new HashSet<>();

    public JsonWebKeySet(InputStream inputStream) {
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject jsonObject = jsonReader.readObject();

        JsonArray keysArr = jsonObject.getJsonArray("keys");
        keysArr.stream().map(JsonValue::asJsonObject).map(JsonWebKeySet::map).forEach(keys::add);
    }

    @Override
    public Optional<JsonWebKey> findOneByKeyId(String kid) {
        return keys.stream().filter(key -> Objects.equals(key.getKeyId(), kid)).findAny();
    }

    private static JsonWebKey map(JsonObject object) {
        JsonWebKey webKey = new JsonWebKey();
        for (String parameterKey : object.keySet()) {
            JsonValue value = object.get(parameterKey);
            switch (value.getValueType()) {
                case STRING:
                    webKey.setParameter(parameterKey, object.getString(parameterKey));
                    break;
                case NUMBER:
                    webKey.setParameter(parameterKey, object.getJsonNumber(parameterKey));
                    break;
                case ARRAY:
                    List<String> values = object.getJsonArray(parameterKey).getValuesAs(JsonString::getString);
                    webKey.setParameter(parameterKey, values);
                    break;
            }
        }
        return webKey;
    }

}
