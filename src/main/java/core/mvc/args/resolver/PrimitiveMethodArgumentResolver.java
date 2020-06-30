package core.mvc.args.resolver;

import core.mvc.args.MethodParameter;
import core.utils.typeresolver.PrimitiveTypeResolvers;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.ClassUtils;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class PrimitiveMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean isSupport(MethodParameter parameter) {
        Class<?> parameterType = parameter.getType();
        return ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        return PrimitiveTypeResolvers.resolve(request.getParameter(parameter.getName()), parameter.getType());
    }
}
