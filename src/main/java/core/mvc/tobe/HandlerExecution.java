package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import core.mvc.exception.ReflectionsException;
import core.mvc.tobe.resolver.*;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object controller;
    private HandlerMethodArgumentResolvers handlerMethodArgumentResolvers;

    public HandlerExecution(Method method, Object controller, HandlerMethodArgumentResolvers handlerMethodArgumentResolvers) {
        this.method = method;
        this.controller = controller;
        this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
    }

    private HandlerMethodArgumentResolvers getDefaultHandlerMethodArgumentResolvers(PathPattern pathPattern) {
        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        handlerMethodArgumentResolvers.addResolver(new ServletRequestMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new ServletResponseMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new SimpleDataTypeMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new CommandObjectMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new PathVariableMethodArgumentResolver(pathPattern));
        return handlerMethodArgumentResolvers;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws ReflectionsException {
        try {
            MethodParameters methodParameters = MethodParameters.from(method);
            Object[] args = this.handlerMethodArgumentResolvers.getMethodArguments(methodParameters.getParameters(), request, response);

            return (ModelAndView) method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionsException("unable to invoke method.", e);
        } catch (Exception e) {
            throw new ReflectionsException(e);
        }
    }
}
