package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String CONTROLLER_PACKAGE = "next.controller";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() {
        handlerMappings.add(new LegacyHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping(CONTROLLER_PACKAGE));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        ModelAndView mav;
        try {
            Object handler = getHandler(req);
            if (handler instanceof Controller) {
                mav = ((Controller) handler).execute(req, resp);
            } else if (handler instanceof HandlerExecution) {
                mav = ((HandlerExecution) handler).handle(req, resp);
            } else {
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
            mav.getView().render(mav.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : ", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

}
