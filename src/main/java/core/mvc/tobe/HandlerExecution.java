package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;
    private final Object declaredObject;

    public HandlerExecution(Method method, Object declaredObject) {
        this.method = method;
        this.declaredObject = declaredObject;
    }

    public ModelAndView handle(Object... values) throws Exception {
        return (ModelAndView) method.invoke(declaredObject, values);
    }
}
