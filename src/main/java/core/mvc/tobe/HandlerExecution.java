package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.helper.HandlerMethodHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> clazz;
    private final Method method;
    private final List<HandlerMethodHelper> methodHelpers;

    public HandlerExecution(Class<?> clazz, Method method, List<HandlerMethodHelper> methodHelpers) {
        this.clazz = clazz;
        this.method = method;
        this.methodHelpers = methodHelpers;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
        MethodInfo methodInfo = new MethodInfo(method);
        Object[] bindParameters = methodInfo.bindingParameters(request, methodHelpers);

        return (ModelAndView) method.invoke(clazz.newInstance(), bindParameters);
    }

}
