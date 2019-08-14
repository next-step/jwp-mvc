package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerMappingAdapter implements HandlerAdapter {

    private AnnotationHandlerMapping annotationHandlerMapping;
    private ModelAndViewHandler strategyModelAndViewHandler = new ViewNameModelAndViewHandler();

    public AnnotationHandlerMappingAdapter(String basePackage) {
        this.annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
    }

    public void setStrategyModelAndViewHandler(ModelAndViewHandler strategyModelAndViewHandler) {
        this.strategyModelAndViewHandler = strategyModelAndViewHandler;
    }

    @Override
    public boolean supports(HttpServletRequest req) {
        return annotationHandlerMapping.hasHandler(req);
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handler = annotationHandlerMapping.getHandler(req);
        Object result = handler.handle(req, resp);

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        return strategyModelAndViewHandler.handle(result);
    }
}
