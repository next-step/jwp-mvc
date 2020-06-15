package core.mvc.tobe;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class HandlerArgumentResolver {
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final String JAVA_LANG_PACKAGE_PREFIX = "java.lang";

    public static Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        List<Object> arguments = Lists.newArrayList();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            String parameterName = parameterNames[i];

            log.debug("parameterName: {}, parameterType: {}", parameterName, type);

            if (type.equals(HttpServletRequest.class)) {
                arguments.add(request);
                continue;
            }

            if (type.equals(HttpServletResponse.class)) {
                arguments.add(response);
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

    private static boolean isJavaType(Class<?> type) {
        return Optional.ofNullable(type)
            .filter(t -> t.isPrimitive() || t.getName().startsWith(JAVA_LANG_PACKAGE_PREFIX))
            .isPresent();
    }

    private static void addJavaTypeParameter(HttpServletRequest request, List<Object> arguments, Class<?> type, String parameterName) {
        String parameterValue = getParameterValue(request.getParameterMap(), parameterName);

        if (Objects.nonNull(parameterValue)) {
            arguments.add(getConvertedParameterValue(type, parameterValue));
        }
        else {
            arguments.add(null);
        }
    }

    private static String getParameterValue(Map<String, String[]> requestParameters, String parameterName) {
        if (StringUtils.isEmpty(parameterName) ||
            Objects.isNull(requestParameters) ||
            !requestParameters.containsKey(parameterName)) {
            return null;
        }

        return Arrays.stream(requestParameters.get(parameterName))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    private static Object getConvertedParameterValue(Class type, String parameterValue) {
        return (type.equals(String.class)) ? parameterValue : ConvertUtils.convert(parameterValue, type);
    }

    private static void addCustomObject(
        List<Object> arguments,
        Class<?> type,
        Map<String, String[]> requestParameters) {

        try {
            Object[] parameters = ReflectionUtils.extractFromMultiValuedMap(requestParameters, type.getDeclaredFields());
            Object instance = ReflectionUtils.newInstance(type, parameters);

            if (Objects.nonNull(instance)) {
                arguments.add(instance);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
