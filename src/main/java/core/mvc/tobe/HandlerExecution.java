package core.mvc.tobe;

import core.web.view.ModelAndView;

import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private final Object locatedInstance;
    private final Method method;

    public HandlerExecution(Object locatedInstance, Method method) {
        this.locatedInstance = locatedInstance;
        this.method = method;
    }

    public ModelAndView handle(List<Object> parameters) throws Exception {
        if (parameters.isEmpty()) {
            return (ModelAndView) method.invoke(locatedInstance);
        }

        if (parameters.size() == 1) {
            return (ModelAndView) method.invoke(locatedInstance, parameters.get(0));
        }

        if (parameters.size() == 2) {
            return (ModelAndView) method.invoke(locatedInstance, parameters.get(0), parameters.get(1));
        }

        if (parameters.size() == 3) {
            return (ModelAndView) method.invoke(locatedInstance, parameters.get(0), parameters.get(1), parameters.get(2));
        }

        if (parameters.size() == 4) {
            return (ModelAndView) method.invoke(locatedInstance, parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
        }

        // TODO handler parameter 더 많은 경우
        throw new IllegalArgumentException();
    }

    public Method getMethod() {
        return method;
    }
}
