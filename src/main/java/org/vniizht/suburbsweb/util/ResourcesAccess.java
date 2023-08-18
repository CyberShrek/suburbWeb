package org.vniizht.suburbsweb.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class ResourcesAccess {
    public static JsonNode getJsonConfig(String configName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(
                ResourcesAccess.class.getResourceAsStream("/static/config/" + configName));
    }
}