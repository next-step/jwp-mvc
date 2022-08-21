package core.annotation.web;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RequestMethod {
    NONE,
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static final Set<RequestMethod> REQUEST_METHODS = Arrays.stream(RequestMethod.values())
            .filter(it -> it != NONE)
            .collect(Collectors.toSet());
}
