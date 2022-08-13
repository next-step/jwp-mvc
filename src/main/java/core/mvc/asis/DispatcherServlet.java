package core.mvc.asis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import core.mvc.tobe.Controller;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
        LegacyHandlerMapping lhm = new LegacyHandlerMapping();
        lhm.initMapping();

        AnnotationHandlerMapping  ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();

        handlerMappings.add(ahm);
        handlerMappings.add(lhm);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Controller controller = getHandler(req);
            ModelAndView mav = controller.execute(req, resp);
            mav.getView().render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new HandlerMappingServiceException(e.getMessage());
        }
    }

    private Controller getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(handler -> (Controller) handler.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NotFoundHandlerException::new);
    }
}
