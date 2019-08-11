package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class AnnotationHandlerMappingAdapter implements HandlerAdapter {

    private AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerMappingAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
        annotationHandlerMapping.initialize();
    }

    @Override
    public boolean supports(HttpServletRequest req) {
        return annotationHandlerMapping.getHandler(req) != null;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handler = annotationHandlerMapping.getHandler(req);
        Object result = handler.handle(req, resp);

        if (result instanceof ModelAndView) {
            ModelAndView mav = (ModelAndView) result;
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } else if (result instanceof String) {
            String viewName = (String) result;
            JspView jspView = new JspView(viewName);
            jspView.render(new HashMap<>(), req, resp);
        }


    }
}
