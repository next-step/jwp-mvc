package core.mvc.tobe;

import core.mvc.HandleView;
import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    Class<?> clazz;
    Method executionMethod;

    public HandlerExecution(Class<?> clazz, Method executionMethod) {
        this.clazz = clazz;
        this.executionMethod = executionMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        Class<?> returnObjectType = executionMethod.getReturnType();

        try {
            Object returnObject = executionMethod.invoke(clazz.newInstance(), request, response);
            if (returnObjectType.equals(String.class)) {
                String viewName = (String) returnObject;
                View views = new HandleView(viewName);
                return new ModelAndView(views);
            }

            if (returnObjectType.equals(ModelAndView.class)) {
                return (ModelAndView) returnObject;
            }
        }catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        } catch (InstantiationException e) {

        }

        return null;
    }
}
