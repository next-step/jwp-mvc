package core.mvc;

import core.mvc.asis.LegacyMappingAdapter;
import core.mvc.tobe.*;
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

import static core.mvc.tobe.Environment.ANNOTATION_PACKAGE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final List<HandlerMapping> mappingHandlers;
    private static final Environment environment = Environment.ofDefault();

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    static {
        String annotationPackage = environment.getProperty(ANNOTATION_PACKAGE);
        mappingHandlers = Arrays.asList(new LegacyMappingAdapter(), new AnnotationHandlerMapping(annotationPackage));
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
