package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> mappings = Lists.newArrayList();

    @Override
    public void init() {
        RequestMapping rm = new RequestMapping();
        rm.initMapping();
        AnnotationHandlerMapping am = new AnnotationHandlerMapping();
        am.initialize();

        mappings.add(rm);
        mappings.add(am);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);

        if (handler instanceof Controller) {
            try {
                String viewName = ((Controller) handler).execute(req, resp);
                move(viewName, req, resp);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else if (handler instanceof HandlerExecution) {
            try {
                ModelAndView mav = ((HandlerExecution) handler).handle(req, resp);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("존재하지 않음.");
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
        for (HandlerMapping handlerMapping : mappings) {
            Object handler = handlerMapping.getHandler(req);

            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
