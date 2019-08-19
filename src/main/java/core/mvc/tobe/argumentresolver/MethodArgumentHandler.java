package core.mvc.tobe.argumentresolver;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.ModifyAbleHttpServletRequest;
import core.mvc.tobe.PathPatternUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class MethodArgumentHandler {
    private Logger logger = LoggerFactory.getLogger(MethodArgumentHandler.class);
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = Lists.newArrayList();

    public void add(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
    }

    public Object[] getValues(Method method, HttpServletRequest request, HttpServletResponse response) {
        ModifyAbleHttpServletRequest modifyAbleHttpServletRequest = new ModifyAbleHttpServletRequest(request);
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameters.length];

        addPathVariableParameter(modifyAbleHttpServletRequest, method, parameterNames);
        for (int i = 0; i < parameters.length; i++) {
            logger.debug("parameter : {}", parameterNames[i]);
            values[i] = getParameterValue(modifyAbleHttpServletRequest, response, parameters[i], parameterNames[i]);
        }

        return values;
    }

    private void addPathVariableParameter(ModifyAbleHttpServletRequest request, Method method, String[] parameterNames) {
        Parameter[] parameters = method.getParameters();
        String pathPatternString = getPathPatternString(method);
        String path = request.getRequestURI();

        Map<String, String> variables = PathPatternUtils.getValues(pathPatternString, path);
        for (int i = 0; i < parameters.length; i++) {
            setParameter(request, parameterNames[i], parameters[i], variables);
        }
    }

    private void setParameter(ModifyAbleHttpServletRequest request, String parameterName, Parameter parameter, Map<String, String> variables) {
        if (!parameter.isAnnotationPresent(PathVariable.class)) {
            return;
        }

        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        String key = Strings.isNullOrEmpty(pathVariable.name()) ? parameterName : pathVariable.name();
        String value = variables.get(key);
        request.setParameter(key, value);
    }

    private String getPathPatternString(Method method) {
        return method.getAnnotation(RequestMapping.class).value();
    }

    private Object getParameterValue(ModifyAbleHttpServletRequest request, HttpServletResponse response, Parameter parameter, String parameterName) {
        if (CollectionUtils.isEmpty(handlerMethodArgumentResolvers)) {
            return null;
        }

        return handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .map(resolver -> resolver.resolveArgument(request, response, parameter, parameterName))
                .findFirst()
                .orElse(request.getParameter(parameterName));
    }
}
