package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public  Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<?> constructor = declaringClass.getConstructor();
        Object newInstance = constructor.newInstance();
        return method.invoke(newInstance, request, response);
    }
}
