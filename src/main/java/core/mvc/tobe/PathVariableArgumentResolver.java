package core.mvc.tobe;

import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

import static core.mvc.tobe.ParameterUtils.decideParameter;
import static core.mvc.tobe.ParameterUtils.parsePath;
import static core.mvc.tobe.ParameterUtils.toPathContainer;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {

        return methodParameter.isPathVariable();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestMapping annotation = methodParameter.getMethod().getAnnotation(RequestMapping.class);
        Map<String, String> variables = parsePath(annotation.value())
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();

        return decideParameter(new ArrayList<>(variables.values()).get(0), methodParameter.getType());
    }

}
