package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution implements HandlerAdapter {

    private Object bean;
    private Method method;

    public HandlerExecution() {
    }

    public HandlerExecution(Object bean, Method method) {
        Assert.notNull(bean, "Bean이 null이어선 안됩니다.");
        Assert.notNull(method, "Method가 null이어선 안됩니다.");
        this.bean = bean;
        this.method = method;
    }

    @Override
    public boolean support(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((HandlerExecution) handler).execute(request, response);
    }

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(bean, request, response);
    }
}
