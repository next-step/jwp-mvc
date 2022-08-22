package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.ExecuteHandler;
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
    private static final String BASE_PACKAGE_PATH = "next.controller";

    private List<HandlerMapping> handlerMappings;
    @Override
    public void init() {
        handlerMappings = Arrays.asList(new LegacyHandlerMapping(), new AnnotationHandlerMapping(BASE_PACKAGE_PATH));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            handleRequestMapping(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleRequestMapping(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            ExecuteHandler executeHandler = handlerMapping.findHandler(req);
            if (executeHandler == null) {
                continue;
            }
            ModelAndView modelAndView = executeHandler.handle(req, resp);
            modelAndView.render(req, resp);
            break;
        }
    }
}
