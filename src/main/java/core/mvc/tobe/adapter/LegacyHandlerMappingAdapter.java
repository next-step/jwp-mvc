package core.mvc.tobe.adapter;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyHandlerMappingAdapter implements HandlerAdapter {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handlerMapping) throws Exception {
        Controller controller = (Controller) handlerMapping;
        String viewPath = controller.execute(request, response);
        return new ModelAndView(new JspView(viewPath));
    }

    @Override
    public boolean support(Object object) {
        return (object instanceof Controller);
    }
}
