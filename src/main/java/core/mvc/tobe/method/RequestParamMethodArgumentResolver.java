package core.mvc.tobe.method;

import core.annotation.web.RequestParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final boolean useDefaultResolution;
    private final SimpleTypeConverter converter;

    private RequestParamMethodArgumentResolver(boolean useDefaultResolution, SimpleTypeConverter converter) {
        Assert.notNull(converter, "'converter' must not be null");
        this.useDefaultResolution = useDefaultResolution;
        this.converter = converter;
    }

    public static RequestParamMethodArgumentResolver of(boolean useDefaultResolution, SimpleTypeConverter converter) {
        return new RequestParamMethodArgumentResolver(useDefaultResolution, converter);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            return true;
        }
        if (useDefaultResolution) {
            return parameter.doesNotHaveAnnotations() && converter.isSupports(parameter.type());
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
        String requestParameter = request.getParameter(parameter.name());
        validateParameter(parameter.name(), requestParameter);
        return converter.convert(requestParameter, parameter.type());
    }

    private Object resolveRequestParam(MethodParameter parameter, HttpServletRequest request) {
        RequestParam requestParamAnnotation = parameter.parameterAnnotation(RequestParam.class);
        String name = parameterName(parameter, requestParamAnnotation);
        String requestParameter = request.getParameter(name);
        if (requestParamAnnotation.required()) {
            validateParameter(name, requestParameter);
        }
        return converter.convert(requestParameter, parameter.type());
    }

    private String parameterName(MethodParameter parameter, RequestParam requestParam) {
        if (StringUtils.isBlank(requestParam.value())) {
            return parameter.name();
        }
        return requestParam.name();
    }

    private void validateParameter(String name, String parameter) {
        if (StringUtils.isBlank(parameter)) {
            throw new IllegalArgumentException(String.format("parameter(%s) is required", name));
        }
    }
}
