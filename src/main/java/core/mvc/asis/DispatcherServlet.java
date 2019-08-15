package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.NotFoundServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final List<HandlerMapping> mappingHandlers;

    static {
        mappingHandlers = Arrays.asList(new LegacyMappingAdapter(), AnnotationHandlerMapping.of());
    }

    @Override
    public void init() {
        mappingHandlers.stream()
                .forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        ModelAndView handle = mappingHandlers.stream()
                .filter(handlerMapping -> handlerMapping.isExistHandler(req))
                .findFirst()
                .map(handlerMapping -> handlerMapping.handle(req, resp))
                .orElseThrow(NotFoundServletException::new);

        try {
            handle.getView().render(handle.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
