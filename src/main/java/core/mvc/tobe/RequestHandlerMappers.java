package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestHandlerMappers {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerMappers.class);

    private final List<RequestHandlerMapping> mappers;

    public RequestHandlerMappers() {
        mappers = new ArrayList<>();
    }

    public void addMapper(RequestHandlerMapping mapper) {
        mapper.initialize();
        mappers.add(mapper);
    }

    public ModelAndView mapperHandling(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Object handler = getHandler(request);
            logger.debug("handler: {}", handler);

            HandlerCommand handlerCommand = HandlerCommandFactory.create(handler, request, response);
            return handlerCommand.execute(handler);

        } catch (Throwable e) {
            logger.error("Exception: {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) throws ServletException {
        return
                mappers.stream()
                        .map(m -> m.getHandler(request))
                        .filter(m -> m != null)
                        .findFirst()
                        .orElseThrow(() -> new ServletException("handler does not exists"));
    }

}
