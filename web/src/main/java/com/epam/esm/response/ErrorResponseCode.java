package com.epam.esm.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseCode {

    public static String errorResponseCode(int errorCode) {
        Map<Integer, String> map = new HashMap<>();
        map.put(40400, "Object was not found");
        map.put(40401, "Object was not found with id");
        map.put(40402, "Object was not found with name");
        map.put(40404, "Error with saving object");
        map.put(405, "Method not allowed");
        return map.get(errorCode);
    }
}
