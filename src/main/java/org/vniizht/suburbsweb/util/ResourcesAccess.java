package org.vniizht.suburbsweb.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public abstract class ResourcesAccess {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getJson(String filename) {
        try {
            return objectMapper.readTree(
                    ResourcesAccess.class.getResourceAsStream("/static/" + filename));
        }
        catch (IOException exception){
            return objectMapper.missingNode();
        }
    }

    public static Map<String, String> getMap(String filename) {
        return objectMapper.convertValue(getJson(filename), Map.class);
    }

    public static List<Object> getList(String filename) {
        return objectMapper.convertValue(getJson(filename), List.class);
    }

    public static String getText(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + fileName);
            byte[] fileBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(fileBytes, StandardCharsets.UTF_8);
        }catch (IOException e){
            return "";
        }
    }

    public static String getFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    public static Map<String, Object> getMapFromFile(File file) throws IOException {
        return objectMapper.readValue(file, Map.class);
    }
}
