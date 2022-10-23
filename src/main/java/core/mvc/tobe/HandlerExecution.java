package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class HandlerExecution {

    private Object controller;
    private Method method;

    private static List<MethodArgumentResolver> list = List.of(
            new UserObjectTypeArgumentResolver(),
            new PrimitiveTypeArgumentResolver(),
            new HttpServletArgumentResolver(),
            new PathVariableArgumentResolver(),
            new HttpSessionResolver()
    );

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        Object[] parameterObjs = new Object[parameterNames.length];

        if(parameterObjs.length == 0) {
            return (ModelAndView) method.invoke(controller);
        }

        for (int i = 0; i < parameterObjs.length; ++i) {
            MethodParameter methodParameter = new MethodParameter(method, parameterNames[i], parameters[i]);
            parameterObjs[i] = getParameterObject(request, response, methodParameter);
        }

        return (ModelAndView) method.invoke(controller, parameterObjs);
    }

    private Object getParameterObject(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) throws IOException {
        for (MethodArgumentResolver methodArgumentResolver : list) {
            if(methodArgumentResolver.supportsParameter(methodParameter)) {
                System.out.println("methodArgumentResolver -> {} " + methodArgumentResolver);
                return methodArgumentResolver.resolveArgument(methodParameter, request, response);
            }
        }

        // TODO
        return null;
    }
}
