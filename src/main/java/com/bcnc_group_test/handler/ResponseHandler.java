package com.bcnc_group_test.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Generates a standardized HTTP response entity.
     *
     * @param message     The message to include in the response.
     * @param httpStatus  The HTTP status code for the response.
     * @param responseObj The data object to include in the response.
     * @param count       The total character count to include in the response.
     * @return A {@link ResponseEntity} representing the constructed HTTP response.
     */
    public static ResponseEntity<Map<String, Object>> generateResponse(String message, HttpStatus httpStatus, Object responseObj, int count) {
        Map<String, Object> map = createBaseResponseMap(message, httpStatus);
        map.put("data", responseObj);
        map.put("count", count);

        return new ResponseEntity<>(map, httpStatus);
    }

    /**
     * Generates a standardized HTTP response entity.
     *
     * @param message    The message to include in the response.
     * @param httpStatus The HTTP status code for the response.
     * @return A {@link ResponseEntity} representing the constructed HTTP response.
     */
    public static ResponseEntity<Map<String, Object>> generateResponse(String message, HttpStatus httpStatus) {
        Map<String, Object> map = createBaseResponseMap(message, httpStatus);

        return new ResponseEntity<>(map, httpStatus);
    }

    /**
     * Creates the base response map with common fields.
     *
     * @param message    The message to include in the response.
     * @param httpStatus The HTTP status code for the response.
     * @return A map containing the base response fields.
     */
    private static Map<String, Object> createBaseResponseMap(String message, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());

        return map;
    }
}
