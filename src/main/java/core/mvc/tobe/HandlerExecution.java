package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution extends HandlerMethod {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    public HandlerExecution(Object instance, Method method) {
        super(instance, method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) this.invoke(request, response);
    }

    private Object invoke(Object... providedArguments) {
        Object[] args = getMethodArgumentValues(providedArguments);
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error(e.getTargetException().getMessage(), e.getTargetException());
            throw new RuntimeException(e);
        }
    }

    private Object[] getMethodArgumentValues(Object[] providedArguments) {
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];

            args[i] = findProvidedArgument(parameter, providedArguments);

            if (args[i] != null) {
                continue;
            }
        }

        return args;
    }

    private static Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (providedArgs == null) {
            return null;
        }

        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }

        return null;
    }
}
