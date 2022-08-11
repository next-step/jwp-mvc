package core.mvc.tobe.handler.mapping;

import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.view.SimpleNameView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object invoker;
    private final Method method;

    public HandlerExecution(Object invoker, Method method) {
        this.invoker = invoker;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return handleRequest(request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("요청 처리를 위한 핸들러 실행중 알수없는 예외가 발생했습니다.", e);
        }
    }

    private ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        Object invokeResult = method.invoke(invoker, request, response);
        if (invokeResult instanceof String) {
            return new ModelAndView(new SimpleNameView((String) invokeResult));
        }

        if (invokeResult instanceof ModelAndView) {
            return (ModelAndView) invokeResult;
        }

        throw new UnSupportExecutionResultTypeException("지원하지않는 타입을 반환할 수 없습니다.");
    }
}
