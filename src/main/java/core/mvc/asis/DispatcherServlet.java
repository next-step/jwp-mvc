package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
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
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private LegacyRequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private List<HandlerMapping> handlerMappingList;

    @Override
    public void init() throws ServletException {
        rm = new LegacyRequestMapping();
        rm.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
        handlerMappingList = List.of(rm, annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = getHandler(req);
            handle(handler, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest req) throws ServletException {
        return handlerMappingList.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findFirst()
                .orElseThrow(() -> new ServletException("요청을 찾을 수 없습니다."));
    }

    private void handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(req, resp);
            move(viewName, req, resp);
        } else if (handler instanceof HandlerExecution) {
            ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);
            move(modelAndView, req, resp);
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

    private void move(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, resp);
    }
}
