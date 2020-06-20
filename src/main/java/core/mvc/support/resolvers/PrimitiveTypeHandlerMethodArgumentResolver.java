package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;
import core.mvc.utils.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Resolver for primitive type without any annotation.
 *
 * @author hyeyoom
 */
public class PrimitiveTypeHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveTypeHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final Class<?> parameterType = methodParameter.getParameterType();
        final boolean isPrimitive = parameterType.isPrimitive();
        return !methodParameter.isAnnotated() && (isPrimitive || String.class.equals(parameterType));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        final String parameter = request.getParameter(methodParameter.getParameterName());
        final Object converted = TypeConverter.convert(parameter, methodParameter.getParameterType());
        log.debug("converted data: {}, type: {}", converted, methodParameter.getParameterType());
        return converted;
    }
}
