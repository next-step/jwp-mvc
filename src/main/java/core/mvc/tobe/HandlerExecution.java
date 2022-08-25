package core.mvc.tobe;

import core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object locatedInstance;
    private final Method method;

    public HandlerExecution(Object locatedInstance, Method method) {
        this.locatedInstance = locatedInstance;
        this.method = method;
    }

    public ModelAndView handle(Object... objects) throws Exception {
        return (ModelAndView) method.invoke(locatedInstance, objects);
    }

    public Method getMethod() {
        return method;
    }
}
