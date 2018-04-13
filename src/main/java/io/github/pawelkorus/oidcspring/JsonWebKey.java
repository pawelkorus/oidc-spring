package io.github.pawelkorus.oidcspring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonWebKey {

    private Map<String, Object> parameters = new HashMap<>();

    public String getAlg() {
        return getStringParameter("alg");
    }

    public void setAlg(String v) {
        setParameter("alg", v);
    }

    public String getKeyType() {
        return getStringParameter("kty");
    }

    public void setKeyType(String v) {
        setParameter("kty", v);
    }

    public String getUse() {
        return getStringParameter("use");
    }

    public void setUse(String v) {
        setParameter("use", v);
    }

    public String getKeyId() {
        return getStringParameter("kid");
    }

    public void setKeyId(String v) {
        setParameter("kid", v);
    }

    public List<String> getX5C() {
        return getListParameter("x5c");
    }

    public void setX5C(List<String> v) {
        setParameter("x5c", v);
    }

    public String getX5T() {
        return getStringParameter("x5t");
    }

    public void setX5T(String v) {
        setParameter("x5t", v);
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public String getStringParameter(String key) {
        Object v = parameters.get(key);
        return v == null ? null : v.toString();
    }

    public List<String> getListParameter(String key) {
        Object v = parameters.get(key);
        return v == null ? null : (List<String>) v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonWebKey that = (JsonWebKey) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }
}
