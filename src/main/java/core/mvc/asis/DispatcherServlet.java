package core.mvc.asis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.exception.HandlerMappingServiceException;
import core.mvc.exception.NotFoundHandlerException;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PACKAGE = "next.controller";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        LegacyHandlerMapping lhm = new LegacyHandlerMapping();
        lhm.initMapping();

        AnnotationHandlerMapping  ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();

        handlerMappings.add(ahm);
        handlerMappings.add(lhm);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);

        try {
            if (handler instanceof HandlerExecution) {
                ModelAndView mav = ((HandlerExecution) handler).handle(req, resp);
                mav.getView().render(mav.getModel(), req, resp);
                return;
            }

            assert handler != null;

            String mav = ((Controller) handler).execute(req, resp);
            move(mav, req, resp);
        } catch (Exception e) {
            throw new HandlerMappingServiceException(e.getMessage());
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

    private Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(handler -> handler.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NotFoundHandlerException::new);
    }
}
