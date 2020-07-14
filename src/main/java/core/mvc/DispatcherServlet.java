package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdpaters;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerMappings.add(new LegacyHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("next.controller"));

        handlerAdpaters = new HandlerAdapters(
                new HandlerExecutionHandlerAdapter(),
                new ControllerHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<?> handler = handlerMappings.getHandler(req);
        if (!handler.isPresent()) {
            resp.setStatus(400);
            return;
        }
        ModelAndView results = null;
        try {
            results = handlerAdpaters.execute(req, resp, handler.get());
        } catch (Exception e) {
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        move(results.getViewName(), req, resp);
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
