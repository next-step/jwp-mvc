package core.mvc.tobe;

import com.google.common.collect.Lists;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class HandlerExecution implements ModelAndViewGettable {
    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = HandlerArgumentResolver.resolve(method, request, response);
        return resolveModelAndView(request, method.invoke(instance, arguments));
    }

    private ModelAndView resolveModelAndView(HttpServletRequest request, Object result) {
        if (result instanceof String) {
            return getModelAndView(request, (String)result);
        }

        return (ModelAndView) result;
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "instance=" + instance +
                ", method=" + method +
                '}';
    }
}
