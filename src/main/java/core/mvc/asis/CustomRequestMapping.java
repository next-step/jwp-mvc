package core.mvc.asis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.tobe.AnnotationHandlerMapping;

public class CustomRequestMapping {

    private AnnotationHandlerMapping annotationHandlerMapping;
    private RequestMapping requestMapping;

    public void init(AnnotationHandlerMapping annotationHandlerMapping, RequestMapping requestMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
        this.annotationHandlerMapping.initialize();

        this.requestMapping = requestMapping;
        this.requestMapping.initMapping();
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        var handler = annotationHandlerMapping.getHandler(req);
        if (handler != null) {
            var modelAndView = handler.handle(req, resp);
            modelAndView.render(req, resp);

            return;
        }

        var controller = requestMapping.findController(req.getRequestURI());
        var viewPath = controller.execute(req, resp);

        var requestDispatcher = req.getRequestDispatcher(viewPath);
        requestDispatcher.forward(req, resp);
    }
}
