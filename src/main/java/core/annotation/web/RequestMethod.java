package core.annotation.web;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethod(String typeName) {
        return Arrays.stream(values())
                .filter(method -> method.name().equals(typeName.toUpperCase()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
