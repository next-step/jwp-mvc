package core.mvc;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerExecution {
    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
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
