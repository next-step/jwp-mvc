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
        Object controller = getController();
        Method[] methods = controller.getClass().getMethods();

        Method targetMethod = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> method.getDeclaredAnnotation(RequestMapping.class).value().equals(request.getRequestURI()))
                .filter(method -> method.getDeclaredAnnotation(RequestMapping.class).method().equals(RequestMethod.valueOf(request.getMethod())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found matched method to the request!"));

        return (ModelAndView) targetMethod.invoke(controller, request, response);
    }

    public Object getController() {
        return controller;
    }
}
