package core.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {

    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstanticateBeans;

    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        beans.putAll(preInstanticateBeans
                .stream()
                .collect(Collectors.toMap(clazz -> clazz, this::newInstanceFromDefaultConstructor)));
    }

    public Map<Class<?>, Object> beansWithAnnotationType(Class<? extends Annotation> targetAnnotation) {
        return beans.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(targetAnnotation))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object newInstanceFromDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor(EMPTY_CLASS_ARRAY).newInstance();
        } catch (Exception e) {
            logger.error(String.format("failed instance(%s) creation", clazz), e);
            throw new BeanCreationException(clazz.getName(), String.format("an error occurred during instance(%s) creation", clazz), e);
        }
    }
}
