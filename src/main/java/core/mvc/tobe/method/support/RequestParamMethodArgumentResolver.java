package core.mvc.tobe.method.support;

import core.annotation.web.RequestParam;
import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final boolean required;
    private final SimpleTypeConverter typeConverter;

    private RequestParamMethodArgumentResolver(boolean required, SimpleTypeConverter typeConverter) {
        Assert.notNull(typeConverter, "typeConverter는 null이어선 안됩니다.");
        this.required = required;
        this.typeConverter = typeConverter;
    }

    public static RequestParamMethodArgumentResolver of(boolean required, SimpleTypeConverter typeConverter) {
        return new RequestParamMethodArgumentResolver(required, typeConverter);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            return true;
        }
        if (!required) {
            return parameter.annotationNotExist() && typeConverter.isSupports(parameter.getType());
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            return resolveRequestParam(parameter, request);
        }
        return resolveSimpleType(parameter, request);
    }

    private Object resolveSimpleType(MethodParameter parameter, HttpServletRequest request) {
        String requestParam = request.getParameter(parameter.getName());
        validateParam(parameter.getName(), requestParam);
        return typeConverter.convert(requestParam, parameter.getType());
    }

    private Object resolveRequestParam(MethodParameter parameter, HttpServletRequest request) {
        RequestParam requestParamAnnotation = parameter.parameterAnnotation(RequestParam.class);
        String name = getParamName(parameter, requestParamAnnotation);
        String requestParam = request.getParameter(name);
        if (requestParamAnnotation.required()) {
            validateParam(name, requestParam);
        }
        return typeConverter.convert(requestParam, parameter.getType());
    }

    private String getParamName(MethodParameter parameter, RequestParam requestParam) {
        if (StringUtils.isBlank(requestParam.value())) {
            return parameter.getName();
        }
        return requestParam.name();
    }

    private void validateParam(String name, String param) {
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException(String.format("인자 (%s)가 null이어선 안됩니다.", name));
        }
    }
}
