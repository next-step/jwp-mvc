package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(Class<?> clazz,Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(clazz.newInstance(), request,response);
        } catch (IllegalAccessException e) {
            logger.error("handle execution error : {}" ,"IllegalAccess");
        } catch (InvocationTargetException e) {
            logger.error("handle execution error : {}" ,"InvocationTarget");
        } catch (InstantiationException e) {
            logger.error("handle execution error : {}" ,"instantiation");
        }
        return new ModelAndView();
    }
}
