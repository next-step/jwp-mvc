package core.mvc.tobe.argumentresolver.custom;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodParameter;
import core.mvc.tobe.argumentresolver.util.PathVariableUtil;
import core.mvc.tobe.argumentresolver.util.PrimitiveParameterUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PathVariableArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.hasPathVariable();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String path = methodParameter.getPath();
        Map<String, String> uriVariables = PathVariableUtil.getUriVariables(path, requestURI);

        return PrimitiveParameterUtil.parseWithType(uriVariables.get(methodParameter.getName()), methodParameter.getType());
    }
}
