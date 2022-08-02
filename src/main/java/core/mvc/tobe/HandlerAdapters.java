package core.mvc.tobe;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public HandlerAdapters() {
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        Reflections reflections = new Reflections("core.mvc.tobe");
        Set<Class<? extends HandlerAdapter>> classes = reflections.getSubTypesOf(HandlerAdapter.class);
        classes.forEach(clazz -> adapters.add(createNewInstance(clazz)));
    }

    private HandlerAdapter createNewInstance(Class<? extends HandlerAdapter> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return adapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }
}
