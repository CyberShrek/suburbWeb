package org.vniizht.suburbsweb.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public abstract class ConfigAccess {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getJson(String fileName) {
        try {
            return objectMapper.readTree(
                    ConfigAccess.class.getResourceAsStream("/static/" + fileName));
        }
        catch (IOException exception){
            return objectMapper.missingNode();
        }
    }

    public static Map<String, String> getMap(String filename) {
        return objectMapper.convertValue(getJson(filename), Map.class);
    }
}
