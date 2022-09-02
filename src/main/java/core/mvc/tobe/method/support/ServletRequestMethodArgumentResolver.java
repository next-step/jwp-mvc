package core.mvc.tobe.method.support;

import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private ServletRequestMethodArgumentResolver() {
    }

    private static class ServletRequestMethodArgumentResolverHolder {
        private static final ServletRequestMethodArgumentResolver INSTANCE = new ServletRequestMethodArgumentResolver();
    }

    public static ServletRequestMethodArgumentResolver getResolver() {
        return ServletRequestMethodArgumentResolverHolder.INSTANCE;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (HttpServletRequest.class.isAssignableFrom(parameter.getType()) ||
                HttpServletResponse.class.isAssignableFrom(parameter.getType()));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        if (ServletRequest.class.isAssignableFrom(parameter.getType())) {
            validateInstance(parameter.getType(), request);
            return request;
        }
        if (ServletResponse.class.isAssignableFrom(parameter.getType())) {
            validateInstance(parameter.getType(), response);
            return response;
        }
        throw new IllegalArgumentException(String.format("지원하지 않는 인자 타입 (%s) 입니다.", parameter));
    }

    private void validateInstance(Class<?> type, Object target) {
        if (!type.isInstance(target)) {
            throw new IllegalStateException(String.format("인스턴스의 타임 (%s)와 주어진 타입 (%s)가 일치하지 않습니다.",
                    target, type));
        }
    }
}
