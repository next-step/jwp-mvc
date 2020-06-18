package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public class AnnotationHandlerExecutor implements HandlerExecutor {
    @Override
    public boolean supportHandler(Object object) {
        return object instanceof AnnotationHandler;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        AnnotationHandler annotationHandler = (AnnotationHandler) object;

        return annotationHandler.handle(request, response);
    }
}
