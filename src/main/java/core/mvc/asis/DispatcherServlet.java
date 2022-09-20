package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.web.exception.NotFoundHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
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

    private List<HandlerMapping> mappingList = new ArrayList<>();

    @Override
    public void init() {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        mappingList.add(legacyHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        mappingList.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);
        try {
            if (handler instanceof Controller) {
                ModelAndView mv = ((Controller) handler).execute(req, resp);
                View view = mv.getView();
                view.render(mv.getModel(), req, resp);
            } else if (handler instanceof HandlerExecution) {
                ModelAndView mv = ((HandlerExecution) handler).handle(req, resp);
                View view = mv.getView();
                view.render(mv.getModel(), req, resp);
            } else {
                throw new NotFoundHandlerException("handler 를 찾을 수 없습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping mapping : mappingList) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }
}
