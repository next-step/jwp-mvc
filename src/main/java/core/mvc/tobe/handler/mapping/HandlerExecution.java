package core.mvc.tobe.handler.mapping;

import core.mvc.tobe.handler.TargetHandlingException;
import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.view.SimpleNameView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

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
        } catch (IllegalAccessException e) {
            throw new TargetHandlingException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Throwable rootCauseException = e.getCause();
            if (Objects.nonNull(rootCauseException)) {
                throw new TargetHandlingException(rootCauseException.getMessage(), rootCauseException);
            }
            throw new Error(e);
        }
    }

    private ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        // TODO: 2022/08/12 컨트롤러의 argument 동적 바인딩 필요
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
