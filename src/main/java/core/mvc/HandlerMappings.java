package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerMappings {
    private static List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping... handlerMappings) {
        this.handlerMappings.addAll(List.of(handlerMappings));
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping it : handlerMappings) {
            Object handler = it.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        throw new NoSuchElementException();
    }
}
