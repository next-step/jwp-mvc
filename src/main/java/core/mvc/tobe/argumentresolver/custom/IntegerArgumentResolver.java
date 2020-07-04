package core.mvc.tobe.argumentresolver.custom;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodParameter;
import core.mvc.tobe.argumentresolver.util.ParameterUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IntegerArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.isIntegerType();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> clazz = methodParameter.getType();
        String parameter = request.getParameter(methodParameter.getName());

        return ParameterUtil.parseWithType(parameter, clazz);
    }
}
