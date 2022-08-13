package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HttpServletArgumentResolver implements ArgumentResiolver {
    private static final Class<HttpServletRequest> REQUEST_CLASS = HttpServletRequest.class;
    private static final Class<HttpServletResponse> RESPONSE_CLASS = HttpServletResponse.class;
    private static final List<Class<?>> SUPPORT_CLASSES = List.of(REQUEST_CLASS, RESPONSE_CLASS);

    @Override
    public boolean support(NamedParameter parameter) {
        return SUPPORT_CLASSES.contains(parameter.getType());
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        if (parameter.isEqualsType(REQUEST_CLASS)) {
            return request;
        }

        if (parameter.isEqualsType(RESPONSE_CLASS)) {
            return response;
        }

        throw new ArgumentResolveFailException("Request / Response 타입 외에 인자는 바인딩 할 수 없습니다.");
    }
}
