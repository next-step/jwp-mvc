package core.mvc.handler;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.support.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        final String[] names = createParameterNames();
        final Class<?>[] types = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final PathPattern pathPattern = createPathPattern();

        final List result = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {

            result.add(new MethodParameter(names[i], types[i], Arrays.asList(parameterAnnotations[i]), pathPattern));
        }

        return Collections.unmodifiableList(result);
    }

    private String[] createParameterNames() {
        final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        return nameDiscoverer.getParameterNames(method);
    }

    private PathPattern createPathPattern() {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);

        return pp.parse(requestMapping.value());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite) throws Exception {
        final Object[] methodArguments = handlerMethodArgumentResolverComposite.resolveParameters(methodParameters, request, response);
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