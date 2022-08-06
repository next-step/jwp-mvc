package core.mvc.resolver;

import core.annotation.Component;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoAnnotationArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotations().length == 0;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        String foundParameter = request.getParameter(parameterName);

        return cast(parameter.getParameterType(), foundParameter);
    }
}
