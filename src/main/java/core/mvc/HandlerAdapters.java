package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class HandlerAdapters {
    private static final Logger logger = LoggerFactory.getLogger(HandlerAdapters.class);
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters = Arrays.asList(handlerAdapters);
    }

    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        logger.debug("handlerAdapters execute - handler: {}", handler);
        HandlerAdapter readyHandler = handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.getHandlerAdapter(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return readyHandler.handle(request, response, handler);
    }
}
