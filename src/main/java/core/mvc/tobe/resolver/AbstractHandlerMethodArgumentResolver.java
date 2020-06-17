package core.mvc.tobe.resolver;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    protected boolean isHttpServletRequestType(Class<?> type) {
        return HttpServletRequest.class.equals(type);
    }

    protected boolean isHttpServletResponseType(Class<?> type) {
        return HttpServletResponse.class.equals(type);
    }

    protected boolean isPrimitiveOrWrapperType(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type);
    }

    protected Object resolveFromRequestParams(Map<String, String[]> requestParams, String name, Class<?> type) {
        return Optional.ofNullable(requestParams)
            .map(p -> p.get(name))
            .filter(ArrayUtils::isNotEmpty)
            .map(Arrays::stream)
            .map(Stream::findFirst)
            .flatMap(values -> values)
            .map(value -> convertValue(type, value))
            .orElse(null);
    }

    protected Object resolve(Map<String, String> requestParams, String name, Class<?> type) {
        return Optional.ofNullable(requestParams)
            .map(p -> p.get(name))
            .filter(Objects::nonNull)
            .map(value -> convertValue(type, value))
            .orElse(null);
    }

    protected Object convertValue(Class<?> type, String value) {
        if (String.class.equals(type)) {
            return value;
        }

        return ConvertUtils.convert(value, type);
    }
}
