package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public class ControllerExecutor implements HandlerExecutor {
    @Override
    public boolean supportHandler(Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        Controller controller = (Controller) object;
        String viewName = controller.execute(request, response);

        return viewName != null ? new ModelAndView(viewName) : null;
    }
}
