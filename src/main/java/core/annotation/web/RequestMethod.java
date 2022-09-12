package core.annotation.web;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, ALL;

    public static RequestMethod requestMethod(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력에 해당하는 Request Method가 존재하지 않습니다, Input Method : " + method));
    }
}
