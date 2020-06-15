package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {
    private Object controller;

    public HandlerExecution(Object newInstance) {
        this.controller = newInstance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method targetMethod = findTargetMethod(request);
        return (ModelAndView) targetMethod.invoke(controller, request, response);
    }

    public Object getController() {
        return controller;
    }

    private Method findTargetMethod(HttpServletRequest request) {
        return Arrays.stream(this.controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> matchUri(request, method))
                .filter(method -> matchMethod(request, method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found matched method to the request!"));
    }

    private boolean matchUri(HttpServletRequest request, Method method) {
        String providedUri = method.getDeclaredAnnotation(RequestMapping.class).value();
        String requestUri = request.getRequestURI();

        return providedUri.equals(requestUri);
    }

    private boolean matchMethod(HttpServletRequest request, Method method) {
        RequestMethod providedMethod = method.getDeclaredAnnotation(RequestMapping.class).method();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return providedMethod.equals(requestMethod);
    }
}
