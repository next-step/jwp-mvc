package core.mvc.tobe;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

class ControllerRegistry {

    private final Map<Class<?>, Object> registry;

    ControllerRegistry(Map<Class<?>, Object> registry) {
        this.registry = registry;
    }

    boolean isEmpty() {
        return this.registry.isEmpty();
    }

    void registerController(Class<?> type, Object controller) {
        this.registry.put(type, controller);
    }

    Set<Class<?>> getAllTypes() {
        return Collections.unmodifiableSet(this.registry.keySet());
    }

    public Object getInstanceByType(Class<?> controllerType) {
        return this.registry.get(controllerType);
    }
}

