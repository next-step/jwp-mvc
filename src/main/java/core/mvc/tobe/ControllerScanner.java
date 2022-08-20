package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

class ControllerScanner {

    private final Object[] basePackages;

    private final ControllerRegistry registry;

    ControllerScanner(Object... basePackages) {
        this.basePackages = basePackages;
        this.registry = new ControllerRegistry(new HashMap<>());
    }

    ControllerRegistry scanControllers() {
        if (registry.isEmpty()) {
            registerControllers(findControllers());
        }

        return registry;
    }

    private void registerControllers(Set<Class<?>> controllers) {
        if (controllers.isEmpty()) {
            return;
        }

        try {
            register(controllers);
        } catch (Exception e) {
            throw new IllegalStateException("컨트롤러 등록 실패", e);
        }
    }

    private void register(Set<Class<?>> controllers) throws InstantiationException, IllegalAccessException {
        for (Class<?> controller : controllers) {
            registry.registerController(controller, controller.newInstance());
        }
    }

    private Set<Class<?>> findControllers() {
        return Arrays.stream(basePackages)
                .map(Reflections::new)
                .map(reflection -> reflection.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
