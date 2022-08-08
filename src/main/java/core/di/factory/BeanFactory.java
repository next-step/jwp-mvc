package core.di.factory;

import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void initialize() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        createDependencies();
    }

    private void createDependencies() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> clazz : preInstanticateBeans) {
            beans.put(clazz, recursive(clazz));
        }
    }

    private Object recursive(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (injectedConstructor == null) {
            return clazz.getDeclaredConstructor().newInstance();
        }

        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();

        Object[] classes = new Object[parameterTypes.length];
        for (int i = 0; i < classes.length; ++i) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterTypes[i], this.preInstanticateBeans);
            classes[i] = recursive(concreteClass);
        }

        return injectedConstructor.newInstance(classes);
    }


}
