package core.mvc;

import core.mvc.exception.HandlerNotFoundException;
import core.mvc.exception.TypeMismatchException;
import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import core.web.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private ApplicationContext applicationContext;

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void init() {
        handlerMappings = applicationContext.getHandlerMappings();
        handlerAdapters = applicationContext.getHandlerAdapters();
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("No adapter for handler [" + handler + "]"));
    }

    private Object getHandler(HttpServletRequest request) throws ServletException, HandlerNotFoundException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException("No handler mapping"));
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Object handler = getHandler(req);
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView mav = handlerAdapter.handle(req, resp, handler);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (HandlerNotFoundException e) {
            resp.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (TypeMismatchException e) {
            resp.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
