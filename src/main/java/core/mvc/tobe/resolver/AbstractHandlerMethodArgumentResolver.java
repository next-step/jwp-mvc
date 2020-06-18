package core.mvc.tobe.resolver;

import next.util.StringUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    protected Object resolveArgument(String parameterValue, Class<?> type) {
        if (StringUtils.isEmpty(parameterValue)) {
            return null;
        }

        return convertValue(parameterValue, type);
    }

    protected Object convertValue(String value, Class<?> type) {
        if (String.class.equals(type)) {
            return value;
        }

        return ConvertUtils.convert(value, type);
    }
}
