package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private final List<ArgumentResolver> argumentResolvers;
    private final Object controller;
    private final Method method;

    public HandlerExecution(List<ArgumentResolver> argumentResolvers, Object controller, Method method) {
        this.argumentResolvers = argumentResolvers;
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, getArguments(request, response));
    }

    private Object[] getArguments(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Method Count: {}", method.getParameterCount());
        
        MethodParameter[] methodParameters = new MethodParameter[method.getParameterCount()];
        Object[] arguments = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            arguments[i] = argumentResolvers.stream()
                    .filter(argumentResolver -> argumentResolver.supportsParameter(methodParameter))
                    .findFirst()
                    .map(argumentResolver -> argumentResolver.resolveArgument(methodParameter, request, response))
                    .orElseThrow(() -> new RuntimeException("ArgumentResolver not found"));
        }

        return arguments;
    }

}
