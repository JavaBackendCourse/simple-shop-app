package org.myprojects.simple_shop_app.utils.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class JsonConverter {
    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(@Qualifier("java8DateTimeObjectMapper") ObjectMapper objectMapper) {
        JsonConverter.objectMapper = objectMapper;
    }

    public static <T> Optional<T> toObject(String json, Class<T> clazz) {
        try {
            T result = objectMapper.readValue(json, clazz);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json read value error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Map<String, Object>> toMap(String json) {
        try {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };

            HashMap<String, Object> result = objectMapper.readValue(json, typeRef);

            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json read value to Map error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> toJson(Object obj) {
        try {
            String result = objectMapper.writeValueAsString(obj);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json write value error: {}", e.getMessage());

            return Optional.empty();
        }
    }


}
