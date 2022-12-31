package org.wm.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wm.exception.ServiceException;

public class ObjectMapperUtil {

    public static ObjectMapper objectMapper() {
        return SpringUtils.getBean(ObjectMapper.class);
    }

    public static String writeValueAsString(Object value) {
        try {
            return objectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    public static  <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper().readValue(content, valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Method to deserialize JSON content from given JSON content String.
     *
     * @throws StreamReadException if underlying input contains invalid content
     *    of type {@link JsonParser} supports (JSON for default case)
     * @throws DatabindException if the input JSON structure does not match structure
     *   expected for result type (or has other mismatch issues)
     */
    public static  <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper().readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

}
