package core.mvc.tobe;

import static core.mvc.tobe.argumentresolver.HandlerMethodArgumentResolverComposite.resolveParameters;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HandlerAdapter.class);

    private final Object declaredObject;
    private final Method method;

    public  HandlerAdapter(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] values = resolveParameters(method, request, response);

        return (ModelAndView) method.invoke(declaredObject, values);
    }
}
