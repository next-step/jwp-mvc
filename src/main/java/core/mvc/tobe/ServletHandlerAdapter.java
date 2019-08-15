package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletHandlerAdapter implements HandlerAdapter {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private HandlerMappings handlerMappings;

    private ServletHandlerAdapter(HandlerMappings handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public static ServletHandlerAdapter of(HandlerMappings handlerMappings) {
        return new ServletHandlerAdapter(handlerMappings);
    }

    @Override
    public boolean support(HttpServletRequest req) {
        return handlerMappings.support(req);
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (handler instanceof Controller) {
            executeWithLegacyController(req, resp, (Controller) handler);
            return;
        }

        if (handler instanceof HandlerExecution) {
            executeWithAnnotation(req, resp, (HandlerExecution) handler);
        }
    }

    private void executeWithAnnotation(HttpServletRequest req, HttpServletResponse resp, HandlerExecution handlerExecution) throws Exception {
        ModelAndView modelAndView = handlerExecution.handle(req, resp);
        modelAndView.viewRender(req, resp);
    }

    private void executeWithLegacyController(HttpServletRequest req, HttpServletResponse resp, Controller controller) throws Exception {
        String viewName = controller.execute(req, resp);
        move(viewName, req, resp);
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
