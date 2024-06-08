package com.github.amenski.utility;

import com.github.amenski.Chapa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.Map;

import static com.github.amenski.utility.StringUtils.isBlank;

/**
 * The <code>Util</code> class serves as a helper class for the main {@link Chapa} class.
 */
public class Util {

    private final static Gson JSON_MAPPER = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    /**
     * @param jsonData a json string to be mapped to a Map object.
     * @return A Map object which contains post fields of the provided
     * JSON data.
     */
    public static Map<String, Object> jsonToMap(String jsonData) {
        return JSON_MAPPER.fromJson(jsonData, Map.class);
    }

    public static void putIfNotNull(Map<String, Object> fields, String key, String value) {
        if (isBlank(value)) return;
        fields.put(key, value);
    }

    public static boolean isAnyNull(Map<String, Object> fields, String... keys) {
        if (keys == null) return true;
        if (fields == null || fields.isEmpty()) return false;
        for (String key : keys) {
            if (!fields.containsKey(key) || fields.get(key) == null) return true;
        }
        return false;
    }
}
