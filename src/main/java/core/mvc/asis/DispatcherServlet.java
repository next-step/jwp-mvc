package core.mvc.asis;

import core.mvc.Handler;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.exception.RequestMismatchException;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    @Override
    public void init() {
        handlerMappings = Arrays.asList(
                new ManualRequestMapping(),
                new AnnotationHandlerMapping("next.controller"));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        Handler handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(RequestMismatchException::new);

        try {
            ModelAndView mav = handler.execute(req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e);
        }
    }
}
