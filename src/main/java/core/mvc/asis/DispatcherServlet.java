package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.RequestHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestHandlerMapping rm;

    private RequestHandlerMapping ahm;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initialize();

        ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = (Controller) rm.getHandler(req);

        try {
            if (controller != null) {
                String viewName = controller.execute(req, resp);
                ModelAndView mav = new ModelAndView(new JspView(viewName));
                render(mav, req, resp);
            } else {
                HandlerExecution handler = (HandlerExecution) ahm.getHandler(req);
                render(handler.handle(req, resp), req, resp);
            }
        } catch (Throwable e) {
            logger.error("Exception: {}", e);
            throw new ServletException(e.getMessage());
        }

    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            mav.getView().render(mav.getModel(), request, response);
        } catch (Throwable e) {
            logger.error("Exception: {}", e);
            throw new ServletException(e.getMessage());
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
