package core.mvc.tobe;

import com.google.common.collect.Lists;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
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
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final String JAVA_LANG_PACKAGE_PREFIX = "java.lang";

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = getParameters(method, request, response);
        return resolveModelAndView(request, method.invoke(instance, arguments));
    }

    private ModelAndView resolveModelAndView(HttpServletRequest request, Object result) {
        if (result instanceof String) {
            return getModelAndView(request, (String)result);
        }

        return (ModelAndView) result;
    }

    private Object[] getParameters(Method method, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        List<Object> arguments = Lists.newArrayList();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class type = parameterTypes[i];
            String parameterName = parameterNames[i];

            if (addHttpServletRequest(request, arguments, type)) {
                continue;
            }

            if (addHttpServletResponse(response, arguments, type)) {
                continue;
            }

            if (isJavaType(type)) {
                addJavaTypeParameter(request, arguments, type, parameterName);
                continue;
            }

            addCustomObject(arguments, type, request.getParameterMap());
        }

        return arguments.toArray(new Object[0]);
    }

    private void addJavaTypeParameter(HttpServletRequest request, List<Object> arguments, Class type, String parameterName) {
        String parameterValue = getParameterValue(parameterName, request.getParameterMap());

        if (Objects.nonNull(parameterValue)) {
            arguments.add(ConvertUtils.convert(parameterValue, type));
        }
    }

    private boolean addHttpServletResponse(HttpServletResponse response, List<Object> arguments, Class type) {
        if (type.equals(HttpServletResponse.class)) {
            arguments.add(response);
            return true;
        }

        return false;
    }

    private boolean addHttpServletRequest(HttpServletRequest request, List<Object> arguments, Class type) {
        if (type.equals(HttpServletRequest.class)) {
            arguments.add(request);
            return true;
        }

        return false;
    }

    private boolean isJavaType(Class type) {
        return Optional.ofNullable(type)
                .filter(t -> t.isPrimitive() || t.getName().startsWith(JAVA_LANG_PACKAGE_PREFIX))
                .isPresent();
    }

    private String getParameterValue(String parameterName, Map<String, String[]> requestParameters) {
        if (StringUtils.isEmpty(parameterName) ||
            Objects.isNull(requestParameters) ||
            requestParameters.size() <= 0) {
            return null;
        }

        return Arrays.stream(requestParameters.get(parameterName))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    private void addCustomObject(
            List<Object> arguments,
            Class type,
            Map<String, String[]> requestParameters) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        Object instance = type.newInstance();

        for (final Field field : ReflectionUtils.getAllFields(type)) {
            String parameterValue = getParameterValue(field.getName(), requestParameters);
            log.debug("parameterValue: {}", parameterValue);

            if (Objects.nonNull(parameterValue)) {
                PropertyUtils.setProperty(instance, field.getName(), ConvertUtils.convert(parameterValue, field.getType()));
            }
        }

        arguments.add(instance);
    }


    @Override
    public String toString() {
        return "HandlerExecution{" +
                "instance=" + instance +
                ", method=" + method +
                '}';
    }
}
