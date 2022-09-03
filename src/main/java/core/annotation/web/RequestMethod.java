package core.annotation.web;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod requestMethod(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(method))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
