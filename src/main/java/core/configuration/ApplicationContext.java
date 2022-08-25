package core.configuration;

import core.di.scanner.ComponentScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private static final ApplicationContext applicationContext = new ApplicationContext();
    private final ComponentScanner componentScanner = ComponentScanner.getInstance();
    private final Map<String, Object> beans = new HashMap<>();

    private ApplicationContext() {

    }

    public static ApplicationContext getInstance() {
        return applicationContext;
    }

    public void init() {
        Set<Class<?>> clazzSet = componentScanner.getComponents();
        clazzSet.forEach(clazz -> {
            String name = clazz.getName();
            beans.put(name, this.constructInstance(clazz));
        });
    }

    public Object getBean(String name) {
        return this.beans.get(name);
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    private Object constructInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
