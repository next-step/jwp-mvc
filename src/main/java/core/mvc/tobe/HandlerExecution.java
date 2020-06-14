package core.mvc.tobe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class HandlerExecution {
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = getParameters(method, request, response);
        return (ModelAndView) method.invoke(instance, arguments);
    }

    private Object[] getParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        List<Object> injectedParameters = Lists.newArrayList();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameterNames[i];

            boolean shouldStop = addIfDefaultParameter(injectedParameters, parameter, Arrays.asList(request, response));

            if (shouldStop) {
                continue;
            }

            addIfRequestParameter(injectedParameters, parameterName, parameter.getType(), request);
        }

        return injectedParameters.toArray(new Object[0]);
    }

    private boolean addIfDefaultParameter(
        List<Object> container,
        Parameter parameter,
        List<Object> targetObjects
    ) {
        if (Objects.isNull(container) ||
            Objects.isNull(parameter) ||
            Objects.isNull(targetObjects) || targetObjects.isEmpty()) {
            return false;
        }

        for (Object targetObj : targetObjects) {
            if (parameter.getType().equals(targetObj.getClass())) {
                container.add(targetObj);
                return true;
            }
        }

        return false;
    }

    private boolean addIfRequestParameter(List<Object> container, String parameterName, Class parameterType, HttpServletRequest request) {
        Map<String, String[]> requestParameters = request.getParameterMap();
        String parameterValue;

        if (Objects.isNull(requestParameters) ||
            !requestParameters.containsKey(parameterName) ||
            StringUtils.isEmpty(parameterValue = getParameterValue(parameterName, parameterType, requestParameters))
        ) {
            return false;
        }

        log.debug("parameterValue: {}", parameterValue);
        container.add((parameterValue);
    }

    private String getParameterValue(String parameterName, Class parameterType, Map<String, String[]> requestParameters) {
        return Arrays.stream(requestParameters.get(parameterName))
            .filter(param -> param.getClass().equals(parameterType))
            .findFirst()
            .orElse(null);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "instance=" + instance +
                ", method=" + method +
                '}';
    }
}
