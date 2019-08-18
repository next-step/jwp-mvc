package core.mvc.tobe;

import core.exception.NotFoundException;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    static {
        argumentResolvers.add(new HttpServletRequestHandlerMethodArgumentResolver());
        argumentResolvers.add(new ParameterMapHandlerMethodArgumentResolver());
        argumentResolvers.add(new CommandHandlerMethodArgumentResolver());
    }

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final MethodParameters methodParameter = new MethodParameters(method);
        final HttpRequestParameters requestParameters = new HttpRequestParameters(request);

        final HandlerMethodArgumentResolver argumentResolver = getHandlerMethodArgumentResolver(methodParameter, requestParameters);
        final Object[] parameters = argumentResolver.resolveArgument(methodParameter, requestParameters, request, response);

        return (ModelAndView) method.invoke(instance, parameters);
    }

    private HandlerMethodArgumentResolver getHandlerMethodArgumentResolver(final MethodParameters methodParameters, final HttpRequestParameters requestParameters) {
        return argumentResolvers.stream()
                .filter(handlerMethodArgumentResolver -> handlerMethodArgumentResolver.supportsParameter(methodParameters, requestParameters))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
