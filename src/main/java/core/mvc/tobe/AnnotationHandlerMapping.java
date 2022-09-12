package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.resolver.ArgumentResolver;
import core.mvc.resolver.HttpRequestArgumentResolver;
import core.mvc.resolver.HttpResponseArgumentResolver;
import core.mvc.resolver.PathVariableArgumentResolver;
import core.mvc.resolver.PrimitiveTypeArgumentResolver;
import core.mvc.resolver.RequestBodyArgumentResolver;
import core.mvc.resolver.RequestParamArgumentResolver;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private static final List<ArgumentResolver> argumentResolvers = asList(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new RequestBodyArgumentResolver(),
            new PrimitiveTypeArgumentResolver()
    );

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> methods = Sets.newHashSet();

        for (Class<?> clazz : controllers.keySet()) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
            putHandler(methods, controllers);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    public void putHandler(Set<Method> methods, Map<Class<?>, Object> controllers) {
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(new HandlerKey(rm.value(), rm.method()), new HandlerExecution(argumentResolvers, controllers.get(method.getDeclaringClass()), method));
        }
    }
}
