package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PreInstanticateBeans {

    private Set<Class<?>> preInstanticateBeans;

    public PreInstanticateBeans(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    public Map<Class<?>, Object> createBeanObject() {
        return preInstanticateBeans.stream()
                .collect(Collectors.toMap(b -> b, b -> createInstance(b)));
    }

    public Object createInstance(Class<?> clazz) {
        List<Object> objects = new ArrayList<>();
        try {
            Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
            Class conClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
            if (constructor == null) return conClass.newInstance();
            for (Class param : constructor.getParameterTypes()) {
                Object obj = createInstance(param);
                objects.add(obj);
            }

            return constructor.newInstance(objects.toArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
