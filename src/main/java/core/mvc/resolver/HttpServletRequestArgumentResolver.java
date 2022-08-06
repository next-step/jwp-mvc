package core.mvc.resolver;

import core.annotation.Component;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpServletRequestArgumentResolver implements MethodArgumentResolver{
    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        return request;
    }
}
