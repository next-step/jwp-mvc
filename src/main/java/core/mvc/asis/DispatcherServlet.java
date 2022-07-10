package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private HandlerMappings handlerMappings;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings(List.of(new LegacyHandlerMapping(), new AnnotationHandlerMapping("core.mvc.tobe")));
        handlerMappings.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = handlerMappings.getHandler(req);
        try {
            if (handler instanceof Controller) {
                String viewName = ((Controller) handler).execute(req, resp);
                move(viewName, req, resp);
                return;
            }

            if (handler instanceof HandlerExecution) {
                ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);
                Map<String, Object> model = modelAndView.getModel();
                View view = modelAndView.getView();
                view.render(model, req, resp);
                return;
            }
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }

        throw new IllegalArgumentException("지원하지 않은 Handler type 입니다.");
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
