package core.mvc.args.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.args.MethodParameter;
import core.utils.PathVariableParser;
import core.utils.typeresolver.PrimitiveTypeResolvers;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean isSupport(MethodParameter parameter) {
        return parameter.isParameterAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        RequestMapping rm = parameter.getMethodAnnotation(RequestMapping.class);
        Map<String, String> variables = PathVariableParser.parsePathVariable(rm.value(), request.getRequestURI());
        return PrimitiveTypeResolvers.resolve(variables.get(parameter.getName()), parameter.getType());
    }
}
