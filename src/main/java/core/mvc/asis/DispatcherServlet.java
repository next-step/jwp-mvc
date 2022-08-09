package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.resolver.ArgumentResolverMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String FAILED_INITIALIZE_ANNOTATION_HANDLER_MAPPING_MESSAGE = "애노테이션 핸들러 매핑 초기화에 실패했습니다.";
    public static final String INVALID_HANDLER_MESSAGE = "유효한 핸들러가 아닙니다.";
    private AnnotationHandlerMapping ahm;
    private ArgumentResolverMapping arm;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {

        try {
            arm = new ArgumentResolverMapping();
            arm.init();

            ahm = new AnnotationHandlerMapping("next.controller");
            ahm.initialize();
            handlerMappings.add(ahm);

        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            logger.error(FAILED_INITIALIZE_ANNOTATION_HANDLER_MAPPING_MESSAGE);
            throw new ServletException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = findHandler(req);
            ModelAndView mav = handle(handler, req, resp);
            mav.render(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    private Object findHandler(HttpServletRequest req) throws NoSuchMethodException {
        return handlerMappings.stream()
                .filter(hm -> Objects.nonNull(hm.getHandler(req)))
                .findFirst()
                .map(hm-> hm.getHandler(req))
                .orElseThrow(NoSuchMethodException::new);
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof HandlerExecution) {
            Object[] args = arm.resolve(((HandlerExecution) handler).getMethod(), req, resp);
            return ((HandlerExecution)handler).handle(args);
        }
        throw new ServletException(INVALID_HANDLER_MESSAGE);
    }
}
