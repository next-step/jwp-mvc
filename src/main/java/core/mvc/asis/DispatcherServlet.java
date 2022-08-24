package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import core.mvc.view.View;
import exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping requestMapping;

    private AnnotationHandlerMapping AnnotationHandlerMapping;
    private final List<HandlerMapping> mappings = new ArrayList<>();

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        AnnotationHandlerMapping = new AnnotationHandlerMapping();
        AnnotationHandlerMapping.initialize();

        mappings.add(requestMapping);
        mappings.add(AnnotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Object handler = getHandler(request);
        if (handler == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }

        try {
            ModelAndView modelAndView = execute(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof Controller) {
            return ((Controller) handler).execute(request, response);
        }
        return ((HandlerExecution) handler).handle(request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 URL 주소입니다."));
    }
}
