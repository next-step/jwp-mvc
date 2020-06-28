package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.exception.RequestMismatchException;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> handlerMappings;

    @Override
    public void init() throws ServletException {
        handlerMappings = Arrays.asList(
                new ManualRequestMapping(),
                new AnnotationHandlerMapping("next.controller"));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        Object handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(RequestMismatchException::new);

        if (handler instanceof Controller) {
            Controller controller = (Controller) handler;
            String viewName = null;
            try {
                viewName = controller.execute(req, resp);
                move(viewName, req, resp);
            } catch (Exception e) {
                logger.error("Exception : {}", e.getMessage());
                throw new ServletException(e);
            }
        } else {
            HandlerExecution handlerExecution = (HandlerExecution) handler;
            try {
                ModelAndView mav = handlerExecution.handle(req, resp);
                mav.getView().render(mav.getModel(), req, resp);
            } catch (Exception e) {
                logger.error("Exception : {}", e.getMessage());
                throw new ServletException(e);
            }
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
