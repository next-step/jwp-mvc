package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.view.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import java.io.IOException;
import java.util.Optional;
import javassist.NotFoundException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final View EMPTY_VIEW = (model, request, response) -> {};

    private RequestMapping rm;
    private AnnotationHandlerMapping am;

    @Override
    public void init(){
        rm = new RequestMapping();
        rm.initMapping();
        am = new AnnotationHandlerMapping("next.controller");
        am.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            ModelAndView mav = handle(req, resp);
            mav.getView().render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    private ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object handler = getHandler(req);

        if (handler instanceof Controller) {
            return ((Controller)handler).execute(req, resp);
        } else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution)handler).handle(req, resp);
        } else {
            throw new ServletException("not registered");
        }
    }

    private Object getHandler(HttpServletRequest req) {
        return Optional.ofNullable((Object)rm.getHandler(req))
            .orElseGet(()->am.getHandler(req));
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
