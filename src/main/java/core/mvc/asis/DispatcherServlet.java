package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = 1L;
    public static final String CONTROLLER_PACKAGE = "next.controller";

    private HandlerMappings handlerMappings;

    @Override
    public void init() throws ServletException {
        handlerMappings = new HandlerMappings(new RequestMapping(), new AnnotationHandlerMapping(CONTROLLER_PACKAGE));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Object handler = handlerMappings.getHandler(req);
            handle(req, resp, handler);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerAdapter handlerAdapter = HandlerAdapter.of(handler);
        ModelAndView mav = handlerAdapter.handle(handler, req, resp);
        render(mav, req, resp);
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
