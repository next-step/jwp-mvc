package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class HandlerExecution {
    private Object controller;

    public HandlerExecution(Object newInstance) {
        this.controller = newInstance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<Method> targetMethod = findTargetMethod(request);

        if(targetMethod.isPresent()){
            return (ModelAndView) targetMethod.get().invoke(controller, request, response);
        }

        Controller controller = (Controller) this.controller;
        String uri = controller.execute(request, response);
        return new ModelAndView(new JspView(uri));
    }

    public Object getController() {
        return controller;
    }

    private Optional<Method> findTargetMethod(HttpServletRequest request) {
        return Arrays.stream(this.controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> matchUri(request, method))
                .filter(method -> matchMethod(request, method))
                .findFirst();
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
