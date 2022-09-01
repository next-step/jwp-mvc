package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.handlerAdapter.RequestMappingHandlerAdapter;
import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.MethodParameters;
import core.mvc.tobe.method.support.HandlerMethodArgumentResolverComposite;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution extends RequestMappingHandlerAdapter {

    private final HandlerMethodArgumentResolver resolver = HandlerMethodArgumentResolverComposite.getResolverComposite();
    private final Object bean;
    private final Method method;
    private final MethodParameters methodParameters;

    public HandlerExecution(Object bean, Method method) {
        Assert.notNull(bean, "Bean이 null이어선 안됩니다.");
        Assert.notNull(method, "Method가 null이어선 안됩니다.");
        this.bean = bean;
        this.method = method;
        this.methodParameters = MethodParameters.from(method);
    }

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(bean, extractArgs(request, response));
    }

    private Object[] extractArgs(HttpServletRequest request, HttpServletResponse response) {
        return methodParameters.mapToArray(
                parameter -> resolveParameter(request, response, parameter), Object[]::new);
    }

    private Object resolveParameter(HttpServletRequest request, HttpServletResponse response, MethodParameter parameter) {
        if (!resolver.supportsParameter(parameter)) {
            throw new IllegalStateException(String.format("parameter %s를 추출할 수 없습니다.", parameter));
        }
        return resolver.resolveArgument(parameter, request, response);
    }
}
