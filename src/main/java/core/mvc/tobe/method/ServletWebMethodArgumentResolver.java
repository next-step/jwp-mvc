package core.mvc.tobe.method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletWebMethodArgumentResolver implements HandlerMethodArgumentResolver {

    static final ServletWebMethodArgumentResolver INSTANCE = new ServletWebMethodArgumentResolver();

    private ServletWebMethodArgumentResolver() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    static ServletWebMethodArgumentResolver instance() {
        return INSTANCE;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (HttpServletRequest.class.isAssignableFrom(parameter.type()) ||
                HttpServletResponse.class.isAssignableFrom(parameter.type()));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        if (ServletRequest.class.isAssignableFrom(parameter.type())) {
            validateInstance(parameter.type(), request);
            return request;
        }

        if (ServletResponse.class.isAssignableFrom(parameter.type())) {
            validateInstance(parameter.type(), response);
            return response;
        }
        throw new IllegalStateException(String.format(
                "unsupported parameter(%s) type. supportsParameter should be called first", parameter));
    }

    private void validateInstance(Class<?> parameterType, Object target) {
        if (!parameterType.isInstance(target)) {
            throw new IllegalStateException(
                    String.format("current instance(%s) is not of type(%s)", target, parameterType));
        }
    }
}
