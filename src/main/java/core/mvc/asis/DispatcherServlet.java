package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.*;
import core.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next";

    private List<HandlerMapping> handlerMappings = ListUtils.newArrayList(
            new RequestMapping(),
            new AnnotationHandlerMapping(BASE_PACKAGE)
    );
    private List<HandlerAdapter> handlerAdapters = ListUtils.newArrayList(
            new ControllerHandlerAdapter(),
            new DefaultHandlerAdapter()
    );

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            Object handler = findHandler(req);
            ModelAndView modelAndView = executeHandler(handler, req, resp);
            render(modelAndView, req, resp);
        } catch (Exception e) {
            logger.error("Failed to handle request", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView executeHandler(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("Not found adapter to handle"));
        return handlerAdapter.handle(handler, req, resp);
    }

    private Object findHandler(HttpServletRequest req) throws ServletException {
        return handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(req))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new ServletException("Not found resource"));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, resp);
    }
}
