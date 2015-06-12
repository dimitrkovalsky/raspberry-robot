package com.liberty.robot.helpers;

import com.liberty.robot.messages.KeyPressedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmytro_Kovalskyi on 11.07.2014.
 */
public class JsonHelper {
    private static ObjectMapper mapper = new ObjectMapper();


    public static KeyPressedMessage toKeyPressedMessage(Object data) throws IOException {
        return mapper.convertValue(data, KeyPressedMessage.class);
    }
    public static Integer toInteger(Object data) throws IOException {
        return mapper.convertValue(data, Integer.class);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static <T> String toJson(T data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public static <T> T toEntity(String data, Class clazz) {
        try {
            return (T) mapper.readValue(data, clazz);
        } catch (IOException e) {
            System.err.println("[JsonHelper] " + e.getMessage());
            return null;
        }
    }


    public static <T> List<T> parseList(List<String> data , Class clazz) {
        List<T> result = new ArrayList<>(data.size());
        data.forEach(s -> {
            T r = toEntity(s, clazz);
            if (r != null)
                result.add(r);
        });
        return result;
    }
}
