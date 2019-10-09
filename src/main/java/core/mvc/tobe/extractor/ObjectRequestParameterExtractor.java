package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ObjectRequestParameterExtractor extends RequestParameterSafetyExtractor {

    private static final Logger log = LoggerFactory.getLogger(ObjectRequestParameterExtractor.class);

    @Override
    public boolean isSupport(final ParameterInfo parameterInfo) {
        return parameterInfo.matchType(Object.class);
    }

    @Override
    Object safetyExtract(final ParameterInfo parameterInfo,
                         final HttpServletRequest request,
                         final HttpServletResponse response) {
        try {
            final Class<?> type = parameterInfo.getType();
            final Constructor<?> constructor = type.getConstructor();
            final Object instance = constructor.newInstance();

            for (final Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = PrimitiveConverter.convert(field.getType(), request.getParameter(field.getName()));
                field.set(instance, value);
            }

            return instance;
        } catch (final InstantiationException | InvocationTargetException |
                NoSuchMethodException | IllegalAccessException e) {
            log.warn("Object extract failed", e);

            return null;
        }
    }
}
