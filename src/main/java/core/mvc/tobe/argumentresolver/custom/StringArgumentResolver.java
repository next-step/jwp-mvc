package core.mvc.tobe.argumentresolver.custom;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StringArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.getType().equals(String.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter(methodParameter.getName());
    }
}
