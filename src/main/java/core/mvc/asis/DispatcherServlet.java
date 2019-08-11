package core.mvc.asis;

import core.mvc.HandleView;
import core.mvc.ModelAndView;
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

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);


    private RequestMapping rm;
    private AnnotationHandlerMapping handlerMapping;

    @Override
    public void init() {
        rm = new RequestMapping();
        rm.initMapping();

        handlerMapping = new AnnotationHandlerMapping("next");
        handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);
        HandlerExecution execution = handlerMapping.getHandler(req);

        boolean isExecuteController = executeController(req, resp, controller);
        boolean isExecuteAnnotation = executeAnnotation(req, resp, execution);

        if(!isExecuteController && !isExecuteAnnotation){
            throw new ServletException("존재하지 않는 페이지 입니다.");
        }
    }

    private boolean executeController(HttpServletRequest req,
                                   HttpServletResponse resp,
                                   Controller controller) throws ServletException {
        if(controller != null){
            try {
                String viewName = controller.execute(req, resp);
                //move(viewName, req, resp);
                new HandleView(viewName).render(null, req, resp);
                return true;
            } catch (Throwable e) {
                logger.error("Exception : {}", e);
                throw new ServletException(e.getMessage());
            }
        }

        return false;
    }

    private boolean executeAnnotation(HttpServletRequest req,
                                   HttpServletResponse resp,
                                   HandlerExecution execution) throws ServletException {
        if(execution != null){
            try {
                ModelAndView modelAndView = execution.handle(req, resp);
                modelAndView.getView().render(null, req, resp);
                return true;
            } catch (Throwable e) {
                logger.error("Exception : {}", e);
                throw new ServletException(e.getMessage());
            }
        }

        return false;
    }

}
