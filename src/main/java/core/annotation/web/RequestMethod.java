package core.annotation.web;

import core.mvc.tobe.IllegalMethodException;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(RequestMethod method) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(method.toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalMethodException(method.toString()));
    }
}
