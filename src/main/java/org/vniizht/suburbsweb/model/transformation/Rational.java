package org.vniizht.suburbsweb.model.transformation;

import java.util.HashMap;
import java.util.Map;

public abstract class Rational {

    public Object get(String key) {
        if(obviousMap.containsKey(key))
            return obviousMap.get(key);
        else
            throw new IllegalArgumentException("Unknown key: " + key);
    }

    protected void put(String key, Object value) {
        obviousMap.put(key, value);
    }

    private final Map<String, Object> obviousMap = new HashMap<>();
}