package core.mvc.tobe;

import org.reflections.Reflections;

import javax.servlet.ServletException;
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

    public HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return adapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new ServletException(String.format("해당 핸들러를 처리할 어댑터를 찾을 수 없습니다: %s", handler)));
    }
}
