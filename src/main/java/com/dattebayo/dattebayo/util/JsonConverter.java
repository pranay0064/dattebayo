package com.dattebayo.dattebayo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static JsonNode getJsonNode(String jsonString) {

        JsonNode data = null;
        try {
            data = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String getJsonString(JsonNode jsonNode) {

        String data = null;
        try {
            data = mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

}
