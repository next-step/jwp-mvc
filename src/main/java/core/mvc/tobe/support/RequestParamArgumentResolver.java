package core.mvc.tobe.support;

import core.annotation.web.RequestParam;
import core.exception.MethodArgumentTypeNotSupportedException;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class RequestParamArgumentResolver implements ArgumentResolver{
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getAnnotations())
                .anyMatch(ann -> ann.annotationType() == RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParam requestParam = Arrays.stream(methodParameter.getAnnotations())
                .filter(ann -> ann.annotationType() == RequestParam.class)
                .findAny()
                .map(ann -> (RequestParam) ann)
                .orElseThrow(IllegalArgumentException::new);

        Object arg = request.getParameter(requestParam.value());

        if (methodParameter.getType() == String.class) {
            return arg;
        }

        throw new MethodArgumentTypeNotSupportedException(methodParameter.getType(), arg);
    }

}
