package core.mvc.handler;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.support.*;
import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class HandlerExecution {
    private final Object instance;
    private final Method method;
    private final List<MethodParameter> methodParameters;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.methodParameters = createMethodParameters(method);
    }

    private List<MethodParameter> createMethodParameters(Method method) {
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        String[] names = nameDiscoverer.getParameterNames(method);
        Class<?>[] types = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        List result = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            result.add(new MethodParameter(names[i], types[i], parameterAnnotations[i].length == 0 ? null : parameterAnnotations[i][0]));
        }

        return Collections.unmodifiableList(result);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        handlerMethodArgumentResolverComposite.addResolver(new ServletRequestResolver(request));
        handlerMethodArgumentResolverComposite.addResolver(new ServletResponseResolver(response));
        handlerMethodArgumentResolverComposite.addResolver(new RequestParamResolver());

        final Object[] methodArguments = handlerMethodArgumentResolverComposite.resolveParameters(methodParameters, request);
        return (ModelAndView) method.invoke(instance, methodArguments);
    }

    public List<HandlerKey> createHandlerKeys() {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();

        final List<RequestMethod> reuqestMethods = convertMethods(requestMapping);

        return reuqestMethods.stream()
                .map(m -> new HandlerKey(path, m))
                .collect(Collectors.toList());
    }

    private List<RequestMethod> convertMethods(RequestMapping requestMapping) {
        final List<RequestMethod> methods = Arrays.asList(requestMapping.method());
        if (methods.isEmpty()) {
            return Arrays.asList(RequestMethod.values());
        }
        return methods;
    }

}
