package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "core.mvc.tobe";

    private List<HandlerMapping> handlerMappings;

    @Override
    public void init() {
        RegacyHandlerMapping rhm = new RegacyHandlerMapping();
        rhm.initMapping();

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();

        handlerMappings = Arrays.asList(rhm, ahm);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        final Object handler = getHandler(req);
        final ModelAndView mav = handle(handler, req, resp);
        render(mav, req, resp);
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            if (handler instanceof HandlerExecution) {
                return ((HandlerExecution) handler).handle(req, resp);
            }
            return new ModelAndView(new JspView(((Controller) handler).execute(req, resp)));
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest req) throws ServletException {
        return handlerMappings.stream()
                .map(hm -> hm.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(ServletException::new);
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Map<String, Object> model = mav.getModel();
        final View view = mav.getView();

        try {
            view.render(model, req, resp);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

}
