package com.ymb.servlet;

/**
 * Minimal JSON string builder so the project doesn't need an external
 * JSON library (Gson/Jackson) just for a couple of AJAX endpoints.
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");
    }
}
