package core.mvc.supporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerMethodArgumentResolverHelper {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverHelper.class);

    private final HandlerMethodArgumentResolverComposite resolverComposite;

    public HandlerMethodArgumentResolverHelper() {
        resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolvers(
                new PrimitiveTypeMethodArgumentResolver(),
                new JavaBeanMethodArgumentResolver(),
                new PathVariableMethodArgumentResolver(),
                new SessionMethodArgumentResolver()
        );
    }

    public Object[] getMethodArgumentsValues(Method method, HttpServletRequest request) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!this.resolverComposite.supportsParameter(param)) {
                throw new IllegalStateException("No suitable resolver");
            }
            try {
                arguments[i] = resolverComposite.resolveArgument(param, method, request, i);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                throw e;
            }
        }

        return arguments;
    }
}
