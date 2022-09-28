package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.MethodArgumentResolver;
import core.mvc.resolver.UserDefinedTypeArgumentResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private Object controller;
    private Method method;

    private List<MethodArgumentResolver> list = new ArrayList<>();

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;

        list.add(new UserDefinedTypeArgumentResolver());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        Object[] parameterObjs = new Object[parameterNames.length];
        for (int i = 0; i < parameterObjs.length; ++i) {
            MethodParameter methodParameter = new MethodParameter(method, parameterNames[i], parameters[i]);

            parameterObjs[i] = getParameterObject(request, methodParameter);

            if(parameterObjs[i] != null) {
                return (ModelAndView) method.invoke(controller, parameterObjs);
            }
        }

        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Object getParameterObject(HttpServletRequest request, MethodParameter methodParameter) {
        for (MethodArgumentResolver methodArgumentResolver : list) {
            if(methodArgumentResolver.supportsParameter(methodParameter)) {
                return methodArgumentResolver.resolveArgument(methodParameter, request);
            }
        }

        // TODO
        return null;
    }
}
