package core.annotation.web;

import java.util.Arrays;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE
    ;

    public static RequestMethod valueOfMethod(final String requestMethod) {
        return Arrays.stream(values())
                .filter(method -> method.name().equals(requestMethod.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("http request method를 입력해주세요."));
    }
}
