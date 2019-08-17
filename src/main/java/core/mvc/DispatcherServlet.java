package core.mvc;

import core.mvc.asis.LegacyMappingAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandleException;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.NotFoundServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_PACKAGE = "next.controller";

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final List<HandlerMapping> mappingHandlers;

    static {
        mappingHandlers = Arrays.asList(new LegacyMappingAdapter(), new AnnotationHandlerMapping(DEFAULT_PACKAGE));
    }

    @Override
    public void init() {
        mappingHandlers.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            ModelAndView modelAndView = mappingHandlers.stream()
                    .filter(handlerMapping -> handlerMapping.supports(req))
                    .findFirst()
                    .map(serve(req, resp))
                    .orElseThrow(NotFoundServletException::new);

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, resp);
        } catch (NotFoundServletException e) {
            resp.sendError(SC_NOT_FOUND);
        } catch (Exception e) {
            logger.error("View render exception", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Function<HandlerMapping, ModelAndView> serve(HttpServletRequest req, HttpServletResponse resp) {
        return handlerMapping -> {
            try {
                return handlerMapping.handle(req, resp);
            } catch (Exception e) {
                throw new HandleException(e);
            }
        };
    }
}
