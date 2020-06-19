package core.mvc.asis;

import core.mvc.ControllerExecutor;
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

    private List<HandlerMapping> requestMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        RequestMapping rm = new RequestMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.asis", "next.controller");
        annotationHandlerMapping.initialize();
        rm.initialize();

        requestMappings.add(rm);
        requestMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        Object executor = ControllerExecutor.findExecutor(requestMappings, req);
        try {
            if (executor instanceof Controller) {
                modelAndView = new ModelAndView(((Controller) executor).execute(req, resp));
            }

            if (executor instanceof HandlerExecution) {
                modelAndView = ((HandlerExecution) executor).handle(req, resp);
            }

            this.viewRender(modelAndView, req, resp);
        } catch (Exception e) {
            throw new ServletException("Displatcher servlet throw Exception", e);
        }
    }

    private void viewRender(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }

}
