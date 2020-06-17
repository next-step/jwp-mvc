package core.mvc.tobe.resolver;

import core.mvc.tobe.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class CustomArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !isHttpServletRequestType(parameter.getType()) &&
            !isHttpServletResponseType(parameter.getType()) &&
            !ClassUtils.isPrimitiveOrWrapper(parameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return resolve(request.getParameterMap(), parameter.getType());
    }

    private Object resolve(Map<String, String[]> multiValuedMap, Class<?> type) {
        try {
            Object[] parameters = resolveFromMultiValuedMap(multiValuedMap, type.getDeclaredFields());
            return ReflectionUtils.newInstance(type, parameters);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Object[] resolveFromMultiValuedMap(Map<String, String[]> multiValuedMap, Field[] fields) {
        if (MapUtils.isEmpty(multiValuedMap) || ArrayUtils.isEmpty(fields)) {
            return null;
        }

        List<Object> extracted = Arrays.stream(fields)
            .map(field -> resolveFromRequestParams(multiValuedMap, field.getName(), field.getType()))
            .filter(Objects::nonNull)
            .peek(value -> log.debug("value: {}, type: {}", value, value.getClass()))
            .collect(Collectors.toList());

        return extracted.toArray(new Object[0]);
    }
}
