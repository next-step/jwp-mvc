package core.mvc.tobe;

import core.mvc.JspHandleView;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    Class<?> clazz;
    Method executionMethod;

    public HandlerExecution(Class<?> clazz, Method executionMethod) {
        this.clazz = clazz;
        this.executionMethod = executionMethod;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Class<?> returnObjectType = executionMethod.getReturnType();

        try {
            Object returnObject = executionMethod.invoke(clazz.newInstance(), request, response);
            if (returnObjectType.equals(String.class)) {
                String viewName = (String) returnObject;
                View views = new JspHandleView(viewName);
                return new ModelAndView(views);
            }

            if (returnObjectType.equals(ModelAndView.class)) {
                return (ModelAndView) returnObject;
            }
        }catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
            throw new IllegalAccessException("잘못된 접근입니다.");
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new InstantiationException("메소드를 생성중 실패하였습니다.");
        }

        return null;
    }
}
