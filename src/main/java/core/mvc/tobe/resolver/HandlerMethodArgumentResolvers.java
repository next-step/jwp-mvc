package core.mvc.tobe.resolver;


import com.google.common.collect.Lists;
import core.mvc.exception.UnsupportedMethodArgumentException;
import core.mvc.tobe.MethodParameter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerMethodArgumentResolvers {

    private final List<HandlerMethodArgumentResolver> resolvers = Lists.newArrayList();

    private HandlerMethodArgumentResolvers() { }

    public static HandlerMethodArgumentResolvers getDefaultHandlerMethodArgumentResolvers() {
        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        handlerMethodArgumentResolvers.addResolver(new ServletRequestMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new ServletResponseMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new SimpleDataTypeMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new CommandObjectMethodArgumentResolver());
        handlerMethodArgumentResolvers.addResolver(new PathVariableMethodArgumentResolver());
        return handlerMethodArgumentResolvers;
    }

    public void addResolver(HandlerMethodArgumentResolver resolver) {
        this.resolvers.add(resolver);
    }

    public Object[] getMethodArguments(List<MethodParameter> methodParameters, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object[] args = new Object[methodParameters.size()];
        for (int i = 0; i < methodParameters.size(); i++) {
            MethodParameter methodParameter = methodParameters.get(i);
            HandlerMethodArgumentResolver resolver = findHandlerMethodArgumentResolver(methodParameter);
            args[i] = resolver.resolveArgument(methodParameter, req, resp);
        }
        return args;
    }

    public HandlerMethodArgumentResolver findHandlerMethodArgumentResolver(MethodParameter methodParameter) throws UnsupportedMethodArgumentException {
        return this.resolvers.stream()
                .filter(handlerMethodArgumentResolver -> handlerMethodArgumentResolver.supports(methodParameter))
                .findFirst()
                .orElseThrow(UnsupportedMethodArgumentException::new);
    }
}
