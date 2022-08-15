package core.mvc.tobe;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;

public class ControllerExecutor implements Controller {
    private final Object declaredObject;
    private final Method method;

    public ControllerExecutor(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] params = new Object[parameterNames.length];
        int index = 0;

        for (String parameterName : parameterNames) {
            params[index++] = request.getParameter(parameterName);
        }

        ModelAndView mav = (ModelAndView) method.invoke(declaredObject, params);

        mav.getModel().forEach((key, value) -> {
            request.getSession().setAttribute(key, value);

            if (key.equals(UserSessionUtils.USER_DELETE_KEY) && (boolean) value) {
                request.getSession().removeAttribute(UserSessionUtils.USER_SESSION_KEY);
            }
        });

        return mav;
    }
}
