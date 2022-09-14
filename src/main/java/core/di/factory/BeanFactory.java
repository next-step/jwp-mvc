package core.di.factory;

import com.google.common.collect.Maps;
import core.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for(Class<?> bean : preInstanticateBeans) {
            try {
                final Object instance = createInstance(bean);
                beans.put(bean, instance);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                System.out.println(e);
                logger.error("Fail to bean initialize : {}", e.getCause());
                throw new RuntimeException("Fail to bean initialize");
            }
        }
    }

    private Object createInstance(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
        final Constructor<?> constructor = concreteClass.getDeclaredConstructors()[0];
        List<Object> parameters = new ArrayList<>();

        for (Class<?> typeClass : constructor.getParameterTypes()) {
            parameters.add(getParameterByClass(typeClass));
        }

        return constructor.newInstance(parameters.toArray());
    }

    private Object getParameterByClass(Class<?> parameterType) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!beans.containsKey(parameterType)) {
            return createInstance(parameterType);
        }
        return getBean(parameterType);
    }
}
